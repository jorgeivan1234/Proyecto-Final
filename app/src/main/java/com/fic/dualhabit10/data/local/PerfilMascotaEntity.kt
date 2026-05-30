package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfil_mascota")
data class PerfilMascotaEntity(
    @PrimaryKey val id: Int = 1,
    val nombre: String = "",
    val especie: String = "",
    val peso: Float = 10f,
    val edad: Int = 0
)