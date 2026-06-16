package com.fic.dualhabit10.ui.viewmodels

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.HigieneMascotaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class HigieneMascotaViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatebase(application)
    private val dao = db.higieneMascotaDao()
    private val _estadoHigiene = MutableStateFlow(HigieneMascotaEntity(fecha = LocalDate.now().toString()))
    val estadoHigiene: StateFlow<HigieneMascotaEntity> = _estadoHigiene

    private val _diasDesdeBano = MutableStateFlow<Long?>(null)
    val diasDesdeBano: StateFlow<Long?> = _diasDesdeBano

    private val _diasDesdeUnas = MutableStateFlow<Long?>(null)
    val diasDesdeUnas: StateFlow<Long?> = _diasDesdeUnas

    fun cargarDatosDiarios(fecha: String = LocalDate.now().toString()) {
        viewModelScope.launch {
            val registro = dao.obtenerHigienePorFecha(fecha)
            _estadoHigiene.value = registro ?: HigieneMascotaEntity(fecha = fecha)
            calcularAlertas()
        }
    }

    fun actualizarHabitoDiario(tipo: String, completado: Boolean)  {
        viewModelScope.launch {
            val actual = _estadoHigiene.value
            val nuevoRegistro = when (tipo) {
                "dental" -> actual.copy(higieneDental = completado)
                "cepillado" -> actual.copy(cepilladoPelo = completado)
                "entorno" -> actual.copy(limpiezaEntorno = completado)
                else -> actual
            }
            dao.guardarHigiene(nuevoRegistro)
            _estadoHigiene.value = nuevoRegistro
        }
    }

    fun registrarBano(){
        viewModelScope.launch {
            val hoy = LocalDate.now().toString()
            val nuevo = _estadoHigiene.value.copy(ultimoBano = hoy)
            dao.guardarHigiene(nuevo)
            _estadoHigiene.value = nuevo
            calcularAlertasEnMemoria(nuevo)
        }
    }

    fun registrarCorteUnas() {
        viewModelScope.launch {
            val hoy = LocalDate.now().toString()
            val nuevo = _estadoHigiene.value.copy(ultimoCorteUnas = hoy)
            dao.guardarHigiene(nuevo)
            _estadoHigiene.value = nuevo
            calcularAlertasEnMemoria(nuevo)
        }
    }

    private suspend fun calcularAlertasEnMemoria(higiene: HigieneMascotaEntity) {
        val hoy = LocalDate.now()
        val uBano = dao.obtenerUltimoBano()?.let {LocalDate.parse(it) }
        val uUnas = dao.obtenerUltimoCorteUnas()?.let {LocalDate.parse(it) }

        _diasDesdeBano.value = uBano?.let { ChronoUnit.DAYS.between(it, hoy) }
        _diasDesdeUnas.value = uUnas?.let { ChronoUnit.DAYS.between(it, hoy) }
    }


    private suspend fun calcularAlertas() {
        val registroActual = _estadoHigiene.value
        val hoy = LocalDate.now()
        val uBano = (dao.obtenerUltimoBano() ?: registroActual.ultimoBano)?.let {LocalDate.parse(it) }
        val uUnas = (dao.obtenerUltimoCorteUnas()?: registroActual.ultimoCorteUnas)?.let {LocalDate.parse(it) }

        _diasDesdeBano.value = uBano?.let { ChronoUnit.DAYS.between(it, hoy) }
        _diasDesdeUnas.value = uUnas?.let { ChronoUnit.DAYS.between(it, hoy) }
    }
}