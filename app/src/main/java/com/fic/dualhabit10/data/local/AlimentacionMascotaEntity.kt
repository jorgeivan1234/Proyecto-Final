package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_recetas_mascota")
data class AlimentacionMascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val calorias: String,
    val TipoMascota: String,
    val imagenUrl: String,
    val colorHex: String,
)