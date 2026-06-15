package com.fic.dualhabit10.data.model

data class Comida(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val ingredientes: List<String> = emptyList(),
    val pasos: List<String> = emptyList(),
    val calorias: String = "",
    val imagenUrl: String = "",
    val colorHex: String = "#FFF9C4"

)