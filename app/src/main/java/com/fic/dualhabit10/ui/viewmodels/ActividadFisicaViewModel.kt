package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.ActividadFisicaEntity
import com.fic.dualhabit10.data.local.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActividadFisicaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatebase(application)
    private val actividadDao = database.actividadFisicaDao()

    val actividades: StateFlow<List<ActividadFisicaEntity>> = actividadDao.obtenerActividades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        verificarYPrecargarActividades()
    }
    private fun verificarYPrecargarActividades() {
        viewModelScope.launch {
            //comprueba si la bd ya tiene registros
            if (actividadDao.contarActividades() == 0) {
                val listaInicial = listOf(
                    ActividadFisicaEntity(
                        titulo = "canicross Ligero",
                        descripcion = "Trote continuo donde tu perro va sujeto a tu cintura con un arnes elastico. Ideal para mejorar la resistencia cardiovascular de ambos.",
                        duracion = "20-30 min",
                        intensidad = "Media",
                        colorHex = "#FFD0E8FF"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Agility casero",
                        descripcion = "Crea un circulo de obstaculos con conos y vallas bajas. Combina sentadillas (tu) con comandos de salto y velocidad (tu mascota).",
                        duracion = "15 min",
                        intensidad = "Alta",
                        colorHex = "#FFFFF2AF"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Caminata de Intervalos",
                        descripcion = "Alterna 2 minutos de caminata rapida con 1 minuto de trote explosivo. Fortalece las articulaciones de tu perro y quema calorias.",
                        duracion = "40 min",
                        intensidad = "Baja - Media",
                        colorHex = "#FFD2F7E1"
                    ),
                    ActividadFisicaEntity(
                        titulo = "Lanzamiento con Despliegue",
                        descripcion = "Lanza la pelota o jugete favorito. Mientras tu perro corre a buscarlo, haz tantas zancadas o flexiones como puedas hasta que regrese.",
                        duracion = "20 min",
                        intensidad = "Alta",
                        colorHex = "#FFFFD3D3"
                    ),
                )
                //inserta la list de defecto usando DAO
                actividadDao.insertarActividades(listaInicial)
            }
        }
    }
}