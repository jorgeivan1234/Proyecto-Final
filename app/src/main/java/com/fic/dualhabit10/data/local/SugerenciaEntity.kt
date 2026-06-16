package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sugerencias")
data class SugerenciaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mensaje: String,
    val fecha: String
)
