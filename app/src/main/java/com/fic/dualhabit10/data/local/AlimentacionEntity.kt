package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_recetas")
data class AlimentacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val calorias: String,
    val imagenUrl: String,
    val colorHex: String
)