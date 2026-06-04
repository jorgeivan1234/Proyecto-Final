package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.PerfilMascotaEntity
import kotlinx.coroutines.launch

class PerfilMascotaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatebase(application)
    private val perfilDao = database.perfilMacotaDao()

    private val sharedPreferences = application.getSharedPreferences("mascota_prefs", Context.MODE_PRIVATE)

    var nombreMascota by mutableStateOf("")
    var especieMascota by mutableStateOf("Perro")
    var pesoMascota by mutableStateOf("")
    var edadMascota by mutableStateOf("")

    var imagenMascota by mutableStateOf(sharedPreferences.getString("saved_mascota_uri", "") ?: "")

    init {
        viewModelScope.launch {
            perfilDao.obtenerPerfil().collect { perfil ->
                if (perfil != null) {
                    nombreMascota = perfil.nombre
                    especieMascota = perfil.especie
                    pesoMascota = perfil.peso.toString()
                    edadMascota = perfil.edad.toString()
                }
            }
        }
    }

    fun guardarDatos(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Aguarda la ruta de la imagen de perfilmascota
            sharedPreferences.edit().putString("saved_mascota_uri", imagenMascota).apply()

            val perfil = PerfilMascotaEntity(
                nombre = nombreMascota,
                especie = especieMascota,
                peso = pesoMascota.toFloatOrNull() ?: 0f,
                edad = edadMascota.toIntOrNull() ?: 0
            )
            perfilDao.guardarPerfil(perfil)
            onSuccess()
        }
    }
}