package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_sueno")
data class SuenoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fecha: String,
    val horasDormidas: Double,
    val estadoAnimo: String,
)