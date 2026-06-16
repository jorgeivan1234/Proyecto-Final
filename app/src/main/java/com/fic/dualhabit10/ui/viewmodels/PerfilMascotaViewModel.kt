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
import kotlinx.coroutines.flow.first

class PerfilMascotaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatebase(application)
    private val perfilDao = database.perfilMacotaDao()

    private val sharedPreferences = application.getSharedPreferences("mascota_prefs", Context.MODE_PRIVATE)

    var nombreMascota by mutableStateOf("")
    var especieMascota by mutableStateOf("Perro")
    var pesoMascota by mutableStateOf("")
    var edadMascota by mutableStateOf("")
    var sexoMascota by mutableStateOf(value="")

    var imagenMascota by mutableStateOf(sharedPreferences.getString("saved_mascota_uri", "") ?: "")

    init {
        viewModelScope.launch {
            try {
                val perfil = perfilDao.obtenerPerfil().first()
                if (perfil != null) {
                    nombreMascota = perfil.nombre
                    especieMascota = perfil.especie
                    pesoMascota = if (perfil.peso > 0f) perfil.peso.toString() else ""
                    sexoMascota = perfil.sexo
                    edadMascota = if (perfil.edad > 0) perfil.edad.toString() else ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun guardarDatos(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Aguarda la ruta de la imagen de perfilmascota
            sharedPreferences.edit().putString("saved_mascota_uri", imagenMascota).apply()

            val perfil = PerfilMascotaEntity(
                id = 1,
                nombre = nombreMascota,
                especie = especieMascota,
                sexo = sexoMascota,
                peso = pesoMascota.toFloatOrNull() ?: 0f,
                edad = edadMascota.toIntOrNull() ?: 0
            )
            perfilDao.guardarPerfil(perfil)
            onSuccess()
        }
    }
}