package com.fic.dualhabit10.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ActividadFisicaViewModel : ViewModel(){
    private val _actividades = MutableStateFlow<List<ActividadMascota>>(emptyList())
    val actividades: StateFlow<List<ActividadMascota>> = _actividades

    init {
        cargarActividadesDesdeBaseDatos()
    }
    private fun cargarActividadesDesdeBaseDatos(){
        //esto simula la lectura de bd
        _actividades.value = listOf(
            ActividadMascota(
                id = 1,
                titulo = "canicross Ligero",
                descripcion = "Trote continuo donde tu perro va sujeto a tu cintura con un arnes elastico. Ideal para mejorar la resistencia cardiovascular de ambos.",
                duracion = "20-30 min",
                intensidad = "Media",
                colorTarjeta = Color(0xFFD0E8FF)
            ),
            ActividadMascota(
                id = 2,
                titulo = "Agility casero",
                descripcion = "Crea un circulo de obstaculos con conos y vallas bajas. Combina sentadillas (tu) con comandos de salto y velocidad (tu mascota).",
                duracion = "15 min",
                intensidad = "Alta",
                colorTarjeta = Color(0xFFFFF2AF)
            ),
            ActividadMascota(
                id = 3,
                titulo = "Caminata de Intervalos",
                descripcion = "Alterna 2 minutos de caminata rapida con 1 minuto de trote explosivo. Fortalece las articulaciones de tu perro y quema calorias.",
                duracion = "40 min",
                intensidad = "Baja - Media",
                colorTarjeta = Color(0xFFD2F7E1)
            ),
            ActividadMascota(
                id = 4,
                titulo = "Lanzamiento con Despliegue",
                descripcion = "Lanza la pelota o jugete favorito. Mientras tu perro corre a buscarlo, haz tantas zancadas o flexiones como puedas hasta que regrese.",
                duracion = "20 min",
                intensidad = "Alta",
                colorTarjeta = Color(0xFFFFD3D3)
            ),
        )
    }
}