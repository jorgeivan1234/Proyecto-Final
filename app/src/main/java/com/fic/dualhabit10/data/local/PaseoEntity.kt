package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_paseos")
data class PaseoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val minutos: Int,
    val fecha: Long,
    val notas: String
)
