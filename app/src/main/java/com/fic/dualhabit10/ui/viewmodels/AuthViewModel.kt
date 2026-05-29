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

class AuthViewModel : ViewModel(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

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
        //crea la cuenta en la bd firebase
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: ""
                //información extra
                val  datosUsuario = hashMapOf(
                    "uid" to uid,
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "email" to email,
                    "telefono" to telefono,
                    "fecha_nacimiento" to fechaNac
                )
                // guardará la info usando el uid único del usuario como identification
                db.collection("Usuarios").document(uid)
                    .set(datosUsuario)
                    .addOnSuccessListener { onExito() }
                    .addOnFailureListener { e -> onError("Cuenta creada, pero fallo al guardar el perfil: ${e.localizedMessage}") }
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Error al registrar el usuario")
            }
    }
    //función para recuperar la contraseña
    fun enviarEnlaceRecuperacion(email: String, onExito: () -> Unit, onError: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onExito() }
            .addOnFailureListener { e -> onError(e.localizedMessage ?: "Error al enviar correo") }
    }

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
                onError(e.localizedMessage ?: "Correo o contraseña incorrectos")
            }
    }

    // Función para eliminar la cuenta
    fun eliminarCuenta(onSuccess: () -> Unit) {
        val user = auth.currentUser

        if (user != null) {
            _uiState.value = AuthUiState.Loading

            viewModelScope.launch {
                try {
                    // Borrado de datos en Firestore
                    db.collection("Usuarios").document(user.uid).delete().await()

                    // Borrado de cuenta en Firebase Auth
                    user.delete().await()

                    // Proceso final que indica el borrado de manera exitosa
                    _uiState.value = AuthUiState.Success
                    onSuccess()

                } catch (e: FirebaseAuthRecentLoginRequiredException) {
                    // imprime el error
                    println("Firebase reauth required: ${e.localizedMessage}")
                    // Requerimiento de reautenticación
                    _uiState.value = AuthUiState.RequiresReauth
                } catch (e: Exception) {
                    // Advertencia de error durante el proceso
                    _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Ah ocurrido un error al eliminar su cuenta")
                }
            }
        }
    }
    // Esta función solo limpia el estado de los mensajes
    fun resetUiState() {
        _uiState.value = AuthUiState.Idle
    }
}
// Estados de la interfaz durante el proceso
sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    object RequiresReauth : AuthUiState
    data class Error(val theMessage: String) : AuthUiState
}