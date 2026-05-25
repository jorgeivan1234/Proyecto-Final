package com.fic.dualhabit10.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import com.fic.dualhabit10.data.model.ComidaMascota
import kotlinx.coroutines.flow.StateFlow



class AlimentacionMascotaViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _recetas = MutableStateFlow<List<ComidaMascota>>(emptyList())
    val recetas: StateFlow<List<ComidaMascota>> = _recetas

    init{
        escucharRecetas()
    }

    private fun escucharRecetas() {
        db.collection("recetas_mascotas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreMascotas", "Error al traer recetas de mascotas", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    try {
                        val lista = snapshot.toObjects(ComidaMascota::class.java)
                        _recetas.value = lista
                        Log.d("FirestoreMascotas", "Recetas cargadas con exito: ${lista.size}")
                    } catch (e: Exception) {
                        Log.e("FirestoreMascotas", "Error en el mapeo de datos: ", e)
                    }
                }
            }
    }
}