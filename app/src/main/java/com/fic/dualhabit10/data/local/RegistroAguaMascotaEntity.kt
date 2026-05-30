package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial_agua_mascota")
data class RegistroAguaMascotaEntity(
    @PrimaryKey
    val fecha: String,
    val cantidadML: Int,
    val metaML: Int,
    val tipoMascota: String = "Perro"
)