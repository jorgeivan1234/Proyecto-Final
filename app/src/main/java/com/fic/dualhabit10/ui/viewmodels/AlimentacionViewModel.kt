package com.fic.dualhabit10.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fic.dualhabit10.data.model.Comida
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AlimentacionViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _recetas = MutableStateFlow<List<Comida>>(emptyList())
    val recetas: StateFlow<List<Comida>> = _recetas

    init{
        escucharRecetas()
    }

    private fun escucharRecetas() {
        db.collection("recetas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestone", "Error al traer recetas", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val lista = snapshot.toObjects(Comida::class.java)
                    _recetas.value = lista
                }
             }
    }
    fun buscarRecetaPorId(id: Int): Comida? {
        return _recetas.value.find { it.id == id }
    }
}