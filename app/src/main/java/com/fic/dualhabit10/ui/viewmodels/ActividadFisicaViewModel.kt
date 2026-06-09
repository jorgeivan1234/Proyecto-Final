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
                notas = "¡Sesion de juego Fisico completado con exito! Gano $puntosGanados pts."
            )
            paseoDao.insertarPaseo(nuevoRegistroJuego)
        }
    }
    private fun verificarYPrecargarActividades() {
        viewModelScope.launch {
            //comprueba si la bd ya tiene registros
            if (actividadDao.contarActividades() == 0) {
                val listaInicial = listOf(
                    ActividadFisicaEntity(
                        titulo = "canicross Ligero",
                        descripcion = "Trote continuo donde tu perro va sujeto a tu cintura con un arnes elastico. Ideal para mejorar la resistencia cardiovascular de ambos.",
                        duracion = "20 min",
                        intensidad = "Media",
                        colorHex = "#FFD0E8FF",
                        imagenUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTj2atkA12pauKEuYJhfxZ1IOfKBm5eth7lnw&s"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Agility casero",
                        descripcion = "Crea un circulo de obstaculos con conos y vallas bajas. Combina sentadillas (tu) con comandos de salto y velocidad (tu mascota).",
                        duracion = "15 min",
                        intensidad = "Alta",
                        colorHex = "#FFFFF2AF",
                        imagenUrl = "https://www.cimformacion.com/blog/wp-content/uploads/2021/09/perro-competicion-agilidad-min.jpg"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Caminata de Intervalos",
                        descripcion = "Alterna 2 minutos de caminata rapida con 1 minuto de trote explosivo. Fortalece las articulaciones de tu perro y quema calorias.",
                        duracion = "40 min",
                        intensidad = "Baja - Media",
                        colorHex = "#FFD2F7E1",
                        imagenUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2qBO-7Yb9jtiVaHl-ba26LqhmFZUxVUcpAg&s"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Lanzamiento con Despliegue",
                        descripcion = "Lanza la pelota o jugete favorito. Mientras tu perro corre a buscarlo, haz tantas zancadas o flexiones como puedas hasta que regrese.",
                        duracion = "20 min",
                        intensidad = "Alta",
                        colorHex = "#FFFFD3D3",
                        imagenUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-nzRnTkKEKtprxv2hOjFSxAS_2uvJQqxRpA&s"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Yoga Estiramiento Dogi",
                        descripcion = "Sesión guiada de estiramientos corporales profundos donde involucras a tu mascota manteniendo posturas de equilibrio juntos.",
                        duracion = "25 min",
                        intensidad = "Baja",
                        colorHex = "#FFE1BEE7",
                        imagenUrl = "https://www.zooplus.es/magazine/wp-content/uploads/2020/04/Yoga-para-perros.webp"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Frisbee Cardio Trail",
                        descripcion = "Carreras de velocidad en zigzag atrapando discos voladores. Desarrolla agilidad extrema, reflejos y aceleración explosiva en espacios abiertos.",
                        duracion = "30 min",
                        intensidad = "Alta",
                        colorHex = "#FFFFE0B2",
                        imagenUrl = "https://cdn.shopify.com/s/files/1/0268/6861/files/frisbee-para-perros.jpg?v=1737476519"
                    ),
                )
                //inserta la list de defecto usando DAO
                actividadDao.insertarActividades(listaInicial)
            }
        }
    }
}