package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AlimentacionEntity
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.model.Comida
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlimentacionViewModel(application: Application) : AndroidViewModel(application) {
    private val alimentacionDao = AppDatabase.getDatebase(application).alimentacionDao()
    private val _recetas = MutableStateFlow<List<Comida>>(emptyList())
    val recetas: StateFlow<List<Comida>> = _recetas

    init{
        checarYPrecargarRecetas()
        escucharRecetasLocales()
    }

    private fun escucharRecetasLocales() {
        viewModelScope.launch {
            alimentacionDao.obtenerRecetas().collectLatest { entidades ->
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
    fun buscarRecetaPorId(id: Int): Comida? {
        return _recetas.value.find { it.id == id }
    }

    private fun checarYPrecargarRecetas(){
        viewModelScope.launch {
            if (alimentacionDao.cantidadRecetas() == 0) {
                val recetasDefecto = listOf(
                    AlimentacionEntity(
                        nombre = "Pollo con Arroz y Verduras",
                        descripcion = "Un plato equilibrado, alto en proteínas y de fácil digestión para mantener la energía de tu mascota.",
                        ingredientes = listOf("150g de pechuga de pollo cocida y desmenuzada (sin sal), 50g de arroz integral cocido, 30g de zanahoria rallada, 20g de calabacita picada en cubos pequeños"),
                        pasos = listOf("Cocinar la pechuga de pollo en agua hirviendo sin añadir sal, ajo ni cebolla., En otra olla, cocer el arroz integral y las verduras al vapor hasta que estén suaves., Desmenuzar perfectamente el pollo una vez frío para evitar que se ahogue., Mezclar el pollo, el arroz y las verduras en un plato y dejar enfriar a temperatura ambiente antes de servir."),
                        calorias = "380 kcal",
                        imagenUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=500&auto=format&fit=crop",
                        colorHex = "#FFF9C4"
                    ),
                    AlimentacionEntity(
                        nombre = "Salmón con Camote y Chícharos.",
                        descripcion = "Excelente fuente de Omega-3 para un pelaje brillante y salud cardiovascular.",
                        ingredientes = listOf("120g de filete de salmón cocido, 1 camote mediano hervido, una pizca de chicharos al vapor"),
                        pasos = listOf("Hervir el camote hasta que esté suave y cortarlo en cubos., Cocinar el salmón al vapor o a la plancha (sin aceite)., Quitar todas las espinas del pescado meticulosamente., Mezclar todo en el tazón de tu mascota."),
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