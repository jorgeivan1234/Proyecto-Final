package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.SuenoEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class SuenoViewModel(application: Application) : AndroidViewModel(application) {
    private val suenoDao = AppDatabase.getDatebase(application).suenoDao()

    val historialSueno = suenoDao.obtenerHistorialSueno().stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
    )

    var estaDurmiendo by mutableStateOf(false)
        private set

    var tiempoTranscurridoMs by mutableStateOf(0L)
        private set

    private var horaInicioMs = 0L
    private var cronomtroJob: Job? = null

    fun iniciarDescanso() {
        estaDurmiendo = true
        horaInicioMs = System.currentTimeMillis()
        tiempoTranscurridoMs = 0L

        cronomtroJob = viewModelScope.launch {
            while (estaDurmiendo) {
                tiempoTranscurridoMs = System.currentTimeMillis() - horaInicioMs
                delay(1000L)
            }
        }
    }

    fun terminarDescanso (estadoAnimo: String) {
        estaDurmiendo = false
        cronomtroJob?.cancel()

        val horasCalculadas = tiempoTranscurridoMs / (1000 * 60 * 60)
        val horasRedondeadas = (horasCalculadas * 10.0).roundToInt() / 10.0
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaActual = formatoFecha.format(Date())

        viewModelScope.launch {
            suenoDao.insertarSueno(
                SuenoEntity(
                    fecha = fechaActual,
                    horasDormidas = if (horasRedondeadas > 0) horasRedondeadas else 0.1,
                    estadoAnimo = estadoAnimo
                )
            )
            tiempoTranscurridoMs = 0L
        }
    }
}