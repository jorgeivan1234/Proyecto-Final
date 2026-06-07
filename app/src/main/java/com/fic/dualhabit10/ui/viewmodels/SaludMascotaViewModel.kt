package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.SaludMascotaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SaludMascotaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatebase(application)
    private val saludDao = database.saludMascotaDao()

    val historialSalud = saludDao.obtenerHistorialSalud()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    //estados temporales del formulario de registro rapido
    var tipoRegistro by mutableStateOf("Vacuna")
    var nombreRegistro by mutableStateOf("")
    var fechaRegistro by mutableStateOf("")
    var notasRegistro by mutableStateOf("")

    fun agregarRegistro(onCompletion: () -> Unit) {
        if (nombreRegistro.isBlank() || fechaRegistro.isBlank()) return

        viewModelScope.launch {
            val nuevoRegistro = SaludMascotaEntity(
                tipo = tipoRegistro,
                nombre = nombreRegistro,
                fecha = fechaRegistro,
                notas = notasRegistro
            )
            saludDao.insertarRegistroSalud(nuevoRegistro)

            nombreRegistro = ""
            fechaRegistro = ""
            notasRegistro = ""
            onCompletion()
        }
    }

    fun eliminarRegistro(registro: SaludMascotaEntity) {
        viewModelScope.launch {
            saludDao.eliminarRegistroSalud(registro)
        }
    }
}