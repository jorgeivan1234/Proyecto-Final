package com.fic.dualhabit10.data.model

import androidx.compose.ui.graphics.Color

data class ActividadMascota(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val duracion: String,
    val intensidad: String,
    val colorTarjeta: Color
)