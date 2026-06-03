package com.fic.dualhabit10.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// -------------------------------------------------------------------------------------------------
// Constantes de Configuración y mensajes
// -------------------------------------------------------------------------------------------------

// Objeto que centraliza los nombres de las colecciones y campos de la base de datos
private object FirestoreConfig {
    const val COLLECTION_USERS = "Usuarios"

    // Nombres de los campos en el documento del usuario
    const val FIELD_UID = "uid"
    const val FIELD_NOMBRE = "nombre"
    const val FIELD_APELLIDO = "apellido"
    const val FIELD_EMAIL = "email"
    const val FIELD_TELEFONO = "telefono"
    const val FIELD_FECHA_NAC = "fecha_nacimiento"
}

// Objeto que almacena los mensajes de error y registros (logs) del sistema de autenticación
private object AuthMessages {
    const val ERR_PROFILE_SAVE = "Cuenta creada, pero fallo al guardar el perfil: "
    const val ERR_REGISTER = "Error al registrar el usuario"
    const val ERR_SEND_EMAIL = "Error al enviar correo"
    const val ERR_LOGIN = "Correo o contraseña incorrectos"
    const val ERR_DELETE_ACCOUNT = "Ha ocurrido un error al eliminar su cuenta"
    const val LOG_REAUTH = "Firebase reauth required: "
}

// -------------------------------------------------------------------------------------------------
// ViewModel de autenticación
// -------------------------------------------------------------------------------------------------

// AuthViewModel gestiona la lógica de negocio relacionada con la sesión del usuario
class AuthViewModel : ViewModel(){

    // Instancias principales de los servicios de Firebase
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Estado reactivo que la UI observará para mostrar alertas, cargas o errores
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

     // Registra un nuevo usuario en Firebase Auth y posteriormente guarda sus datos personales en Firestore
     // @param nombre - Nombre del usuario
     // @param apellido - Apellido del usuario
     // @param email - Correo electrónico
     // @param pass - Contraseña
     // @param telefono - Número de contacto
     // @param fechaNac - Fecha de nacimiento
     // @param onExito - Callback que se ejecuta si el proceso es exitoso
     // @param onError - Callback que devuelve un mensaje de texto si falla algún proceso
    fun registrarUsuario(
        nombre: String,
        apellido: String,
        email: String,
        pass: String,
        telefono: String,
        fechaNac: String,
        onExito: () -> Unit,
        onError: (String) -> Unit
    ){
        // Crea la cuenta usando correo y contraseña
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: ""

                // Prepara el "paquete" de datos usando las constantes seguras
                val datosUsuario = hashMapOf(
                    FirestoreConfig.FIELD_UID to uid,
                    FirestoreConfig.FIELD_NOMBRE to nombre,
                    FirestoreConfig.FIELD_APELLIDO to apellido,
                    FirestoreConfig.FIELD_EMAIL to email,
                    FirestoreConfig.FIELD_TELEFONO to telefono,
                    FirestoreConfig.FIELD_FECHA_NAC to fechaNac
                )

                // Guarda la información en Firestore vinculada al UID único del usuario
                db.collection(FirestoreConfig.COLLECTION_USERS).document(uid)
                    .set(datosUsuario)
                    .addOnSuccessListener { onExito() }
                    .addOnFailureListener { e ->
                        onError("${AuthMessages.ERR_PROFILE_SAVE}${e.localizedMessage}")
                    }
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: AuthMessages.ERR_REGISTER)
            }
    }

    // Envía un correo electrónico al usuario con un enlace para restablecer su contraseña
    fun enviarEnlaceRecuperacion(email: String, onExito: () -> Unit, onError: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onExito() }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: AuthMessages.ERR_SEND_EMAIL)
            }
    }

    // Inicio de sesión validando las credenciales de Firebase
    fun iniciarSesion (
        email: String,
        pass: String,
        onExito: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                onExito()
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: AuthMessages.ERR_LOGIN)
            }
    }

    // Elimina definitivamente la cuenta del usuario actual.
    // Este es un proceso de dos pasos: primero elimina sus datos personales en Firestore
    // y luego borra la credencial de acceso en Firebase Auth
    // @param onSuccess - Callback ejecutado si la eliminación es exitosa
    fun eliminarCuenta(onSuccess: () -> Unit) {
        val user = auth.currentUser

        if (user != null) {
            _uiState.value = AuthUiState.Loading

            viewModelScope.launch {
                // Proceso del borrado:
                try {
                    // 1. Borrado de datos almacenados en la base de datos
                    db.collection(FirestoreConfig.COLLECTION_USERS).document(user.uid).delete().await()

                    // 2. Eliminación de cuenta en Firebase Authentication
                    user.delete().await()

                    // 3. Notificamos éxito
                    _uiState.value = AuthUiState.Success
                    onSuccess()

                } catch (e: FirebaseAuthRecentLoginRequiredException) {
                    // Si Firebase detecta que la sesión es muy vieja, por seguridad detiene el borrado
                    println("${AuthMessages.LOG_REAUTH}${e.localizedMessage}")
                    _uiState.value = AuthUiState.RequiresReauth

                } catch (e: Exception) {
                    // Cualquier otro error de red o base de datos
                    _uiState.value = AuthUiState.Error(e.localizedMessage ?: AuthMessages.ERR_DELETE_ACCOUNT)
                }
            }
        }
    }

    // Limpia el estado de la interfaz gráfica
    fun resetUiState() {
        _uiState.value = AuthUiState.Idle
    }
}

// -------------------------------------------------------------------------------------------------
// Estados de la interfaz (UI State)
// -------------------------------------------------------------------------------------------------

// Representa los diferentes estados posibles durante una operación de autenticación compleja
// (como borrar la cuenta). La interfaz gráfica "escucha" estos estados para saber qué dibujar
sealed interface AuthUiState {
    object Idle : AuthUiState                  // Sin actividad / Reposo
    object Loading : AuthUiState               // Proceso en ejecución
    object Success : AuthUiState               // Proceso finalizado con éxito
    object RequiresReauth : AuthUiState        // Se necesita reautenticación del usuario
    data class Error(val theMessage: String) : AuthUiState // Notificación de error con mensaje
}