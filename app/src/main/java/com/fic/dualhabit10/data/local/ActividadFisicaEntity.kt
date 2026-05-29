package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades_fiscias")
data class ActividadFisicaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val duracion: String,
    val intensidad: String,
    val colorHex: String
)