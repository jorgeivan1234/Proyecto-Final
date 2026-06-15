package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AlimentacionEntity
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.model.Comida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class AlimentacionViewModel(application: Application) : AndroidViewModel(application) {
    private val alimentacionDao = AppDatabase.getDatebase(application).alimentacionDao()
    private val _recetas = MutableStateFlow<List<Comida>>(emptyList())
    val recetas: StateFlow<List<Comida>> = _recetas

    init {
        checarYPrecargarRecetas()
        escucharRecetasLocales()
    }

    private fun escucharRecetasLocales() {
        viewModelScope.launch {
            alimentacionDao.obtenerRecetas().collectLatest { entidades ->
                // Pasamos los datos directos, sin traducir nada aquí
                val listaModelos = entidades.map { entity ->
                    Comida(
                        id = entity.id,
                        nombre = entity.nombre,
                        descripcion = entity.descripcion,
                        ingredientes = entity.ingredientes,
                        pasos = entity.pasos,
                        calorias = entity.calorias,
                        imagenUrl = entity.imagenUrl,
                        colorHex = entity.colorHex
                    )
                }
                _recetas.value = listaModelos
            }
        }
    }

    private fun checarYPrecargarRecetas() {
        viewModelScope.launch(Dispatchers.IO) {
            if (alimentacionDao.cantidadRecetas() == 0) {
                // Solo insertamos las claves de nuestro diccionario
                val recetasDefecto = listOf(
                    AlimentacionEntity(
                        nombre = "receta_pollo_nombre",
                        descripcion = "receta_pollo_desc",
                        ingredientes = listOf("receta_pollo_ingredientes"),
                        pasos = listOf("receta_pollo_pasos"),
                        calorias = "380 kcal",
                        imagenUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=500&auto=format&fit=crop",
                        colorHex = "#FFF9C4"
                    ),
                    AlimentacionEntity(
                        nombre = "receta_salmon_nombre",
                        descripcion = "receta_salmon_desc",
                        ingredientes = listOf("receta_salmon_ingredientes"),
                        pasos = listOf("receta_salmon_pasos"),
                        calorias = "310 kcal",
                        imagenUrl = "https://media.foodandtravel.mx/wp-content/uploads/2024/03/Receta-salmon.jpg",
                        colorHex = "#FFECB3"
                    )
                )
                alimentacionDao.insertarReceta(recetasDefecto)
            }
        }
    }
}