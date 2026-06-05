package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fic.dualhabit10.data.local.AlimentacionMascotaEntity
import com.fic.dualhabit10.data.local.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import com.fic.dualhabit10.data.model.ComidaMascota
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AlimentacionMascotaViewModel (application: Application) : AndroidViewModel (application) {

    private val mascotaDao = AppDatabase.getDatebase(application).alimentacionMascotaDao()
    private val _recetas = MutableStateFlow<List<AlimentacionMascotaEntity>>(emptyList())
    val recetas: StateFlow<List<AlimentacionMascotaEntity>> = _recetas.asStateFlow()

    fun buscarRecetaPorId(recetaId: Int): kotlinx.coroutines.flow.Flow<AlimentacionMascotaEntity?> {
        return mascotaDao.buscarRecetaPorId(recetaId)
    }
    init{
        escucharRecetasLocales()
    }

    private fun escucharRecetasLocales() {
        viewModelScope.launch {
            if (mascotaDao.cantidadRecetasMascota() == 0) {
                Log.d("RoomMascotas", "Base de datos vacia. Precargando datos de la imagen...")
                precargarDatosMascota()
            }

            mascotaDao.obtenerRecetasMascota().collect { listaLocal ->
                _recetas.value = listaLocal
                Log.d("RoomMascotas", "Recetas cargadas desde Room: ${listaLocal.size}")
            }
        }
    }
    private suspend fun precargarDatosMascota() {
        val listaPrevia = listOf(
            AlimentacionMascotaEntity(
                nombre = "Croquetas Caseras de Pollo",
                descripcion = "Una receta balanceada y deliciosa para consentir a tu lomito",
                calorias = "250 kcal",
                TipoMascota = "Perro",
                imagenUrl = "croquetas_caseras",
                colorHex = "#FFFDE7"
            ),
            AlimentacionMascotaEntity(
                nombre = "Helados Refrescantes de Fresa",
                descripcion = "Premios congelados perfectos para los días calurosos de verano. Ayudan",
                calorias = "80 kcal",
                TipoMascota = "Perro / Gato",
                imagenUrl = "helados_fresa",
                colorHex = "#FCE4EC"
            ),
            AlimentacionMascotaEntity(
                nombre = "Galletas de Avena y Plátano",
                descripcion = "Premios horneados crujientes, fáciles de partir en pedacitos pequeños.",
                calorias = "120 kcal",
                TipoMascota = "Perro",
                imagenUrl = "galletas_avena",
                colorHex = "#F5F5F5"
            ),
            AlimentacionMascotaEntity(
                nombre = "Caldo de Huesos Mágico",
                descripcion = "Ideal para mantener hidratadas las articulaciones y mejorar la digestión.",
                calorias = "95 kcal",
                TipoMascota = "Perro / Gato",
                imagenUrl = "caldo_huesos",
                colorHex = "#FFE0B2"
            )
        )
        mascotaDao.insertarRecetasMascota(listaPrevia)
    }
}