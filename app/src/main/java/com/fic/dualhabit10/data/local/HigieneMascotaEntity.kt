package com.fic.dualhabit10.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "higiene_mascota")
data class HigieneMascotaEntity(
    @PrimaryKey
    val fecha: String,
    val higieneDental: Boolean = false,
    val cepilladoPelo: Boolean = false,
    val limpiezaEntorno: Boolean = false,
    val ultimoBano: String? = null,
    val ultimoCorteUnas: String? = null
)