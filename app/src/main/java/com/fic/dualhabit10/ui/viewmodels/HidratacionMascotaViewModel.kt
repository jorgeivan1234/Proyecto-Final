package com.fic.dualhabit10.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RegistroAguaMascota(
    val fecha: String,
    val cantidadML: Int,
    val metaML: Int
)

class HidratacionMascotaViewModel : ViewModel(){

    private val _metaDiaria = MutableStateFlow(1200)
    val metaDiaria: StateFlow<Int> = _metaDiaria.asStateFlow()

    private val _aguaConsumidaHoy = MutableStateFlow(0)
    val aguaConsumidaHoy: StateFlow<Int> = _aguaConsumidaHoy.asStateFlow()

    private val _historialAgua = MutableStateFlow<List<RegistroAguaMascota>>(emptyList())
    val historiaAgua: StateFlow<List<RegistroAguaMascota>> = _historialAgua.asStateFlow()

    private val fechaHoy: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun agregarAgua(cantidadML: Int) {
        _aguaConsumidaHoy.value += cantidadML
    }
    fun reiniciarProgreso() {
        _aguaConsumidaHoy.value = 0
    }
}