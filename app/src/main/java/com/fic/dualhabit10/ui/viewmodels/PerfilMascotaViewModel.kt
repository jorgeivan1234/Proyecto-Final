package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
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

    var nombreMascota by mutableStateOf("")
    var especieMascota by mutableStateOf("Perro")
    var pesoMascota by mutableStateOf("")
    var edadMascota by mutableStateOf("")

    init{
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