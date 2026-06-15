package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "salud_mascota")
data class SaludMascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo: String,
    val nombre: String,
    val fecha: String,
    val notas: String = ""
)