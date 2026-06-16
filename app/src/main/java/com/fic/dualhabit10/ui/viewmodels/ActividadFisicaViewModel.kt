package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.ActividadFisicaEntity
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.PaseoEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActividadFisicaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatebase(application)
    private val actividadDao = database.actividadFisicaDao()
    private val paseoDao = database.paseoDao()

    var tiempoRestanteKey by mutableStateOf(0)
    var juegoEnProgreso by mutableStateOf(false)
    var juegosCompletados by mutableStateOf(false)
    var puntosGanados by mutableStateOf(0)

    private var cronometroJob: Job? = null

    val actividades: StateFlow<List<ActividadFisicaEntity>> = actividadDao.obtenerActividades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        verificarYPrecargarActividades()
    }

    fun buscarActividadPorId(id: Int): Flow<ActividadFisicaEntity?> {
        return actividadDao.obtenerActividadPorId(id)
    }

    fun iniciarJuego(minutosIniciales: Int) {
        cronometroJob?.cancel()
        tiempoRestanteKey = if (minutosIniciales <= 0) 60 else minutosIniciales * 60
        juegoEnProgreso = true
        juegosCompletados = false
        puntosGanados = 0

        cronometroJob = viewModelScope.launch {
            while (tiempoRestanteKey > 0) {
                delay(1000)
                tiempoRestanteKey--
            }
            terminarGuardarJuego(minutosIniciales)
        }
    }

    fun pausarOdetenerJuego() {
        cronometroJob?.cancel()
        juegoEnProgreso = false
    }

    private fun terminarGuardarJuego(minutosTotales: Int) {
        juegoEnProgreso = false
        juegosCompletados = true
        puntosGanados = minutosTotales * 10

        viewModelScope.launch {
            val nuevoRegistroJuego = PaseoEntity(
                minutos = minutosTotales,
                fecha = System.currentTimeMillis(),
                notas = "nota_juego_completado"
            )
            paseoDao.insertarPaseo(nuevoRegistroJuego)
        }
    }
    private fun verificarYPrecargarActividades() {
        viewModelScope.launch {
            if (actividadDao.contarActividades() == 0) {
                val listaInicial = listOf(
                    ActividadFisicaEntity(
                        titulo = "canicross_ligero_titulo",
                        descripcion = "canicross_ligero_desc",
                        duracion = "duracion_20m",
                        intensidad = "intensidad_media",
                        colorHex = "#FFD0E8FF",
                        imagenUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTj2atkA12pauKEuYJhfxZ1IOfKBm5eth7lnw&s"
                    ),
                    ActividadFisicaEntity(
                        titulo = "agility_casero_titulo",
                        descripcion = "agility_casero_desc",
                        duracion = "duracion_15m",
                        intensidad = "intensidad_alta",
                        colorHex = "#FFFFF2AF",
                        imagenUrl = "https://www.cimformacion.com/blog/wp-content/uploads/2021/09/perro-competicion-agilidad-min.jpg"
                    ),
                    ActividadFisicaEntity(
                        titulo = "caminata_intervalos_titulo",
                        descripcion = "caminata_intervalos_desc",
                        duracion = "duracion_40m",
                        intensidad = "intensidad_baja_media",
                        colorHex = "#FFD2F7E1",
                        imagenUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2qBO-7Yb9jtiVaHl-ba26LqhmFZUxVUcpAg&s"
                    ),
                    ActividadFisicaEntity(
                        titulo = "lanzamiento_despliegue_titulo",
                        descripcion = "lanzamiento_despliegue_desc",
                        duracion = "duracion_20m",
                        intensidad = "intensidad_alta",
                        colorHex = "#FFFFD3D3",
                        imagenUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-nzRnTkKEKtprxv2hOjFSxAS_2uvJQqxRpA&s"
                    ),
                    ActividadFisicaEntity(
                        titulo = "yoga_dogi_titulo",
                        descripcion = "yoga_dogi_desc",
                        duracion = "duracion_25m",
                        intensidad = "intensidad_baja",
                        colorHex = "#FFE1BEE7",
                        imagenUrl = "https://www.zooplus.es/magazine/wp-content/uploads/2020/04/Yoga-para-perros.webp"
                    ),
                    ActividadFisicaEntity(
                        titulo = "frisbee_cardio_titulo",
                        descripcion = "frisbee_cardio_desc",
                        duracion = "duracion_30m",
                        intensidad = "intensidad_alta",
                        colorHex = "#FFFFE0B2",
                        imagenUrl = "https://cdn.shopify.com/s/files/1/0268/6861/files/frisbee-para-perros.jpg?v=1737476519"
                    ),
                )
                actividadDao.insertarActividades(listaInicial)
            }
        }
    }
}