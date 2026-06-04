package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.PaseoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaseosViewModel(application: Application) : AndroidViewModel(application){
    private val paseoDao = AppDatabase.getDatebase(application).paseoDao()

    private val _listaPaseos = MutableStateFlow<List<PaseoEntity>>(emptyList())
    val listaPaseos: StateFlow<List<PaseoEntity>> = _listaPaseos

    init {
        viewModelScope.launch {
            paseoDao.obtenerTodosLosPaseos().collect { paseos ->
                _listaPaseos.value = paseos
            }
        }
    }

    fun agregarPaseo(minutos: Int, notas: String) {
        viewModelScope.launch {
            val fechaActualLong = System.currentTimeMillis()
            val paseo = PaseoEntity(minutos = minutos, fecha = fechaActualLong, notas = notas)
            paseoDao.insertarPaseo(paseo)
        }
    }

    fun eliminarPaseo(paseo: PaseoEntity){
        viewModelScope.launch {
            paseoDao.eliminarPaseo(paseo)
        }
    }
}