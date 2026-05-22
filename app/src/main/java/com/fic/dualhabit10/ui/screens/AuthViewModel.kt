package com.fic.dualhabit10.ui.screens

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                //informacion extra
                val  datosUsuario = hashMapOf(
                    "uid" to uid,
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "email" to email,
                    "telefono" to telefono,
                    "fecha_nacimiento" to fechaNac
                )
                // guardara la info usando el uid unico del usuario como identificaodr
                db.collection("Usuarios").document(uid)
                    .set(datosUsuario)
                    .addOnSuccessListener { onExito() }
                    .addOnFailureListener { e -> onError("Cuenta creada, pero fallo al guardar el perfil: ${e.localizedMessage}") }
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Error al registrar el usuario")
            }
    }
    //funcion para recuperar la contraseña
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
}