package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HigieneMascotaDao {
    @Query("SELECT * FROM higiene_mascota WHERE fecha = :fecha LIMIT 1")
    suspend fun obtenerHigienePorFecha(fecha: String): HigieneMascotaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarHigiene(higiene: HigieneMascotaEntity)

    @Query("SELECT ultimoBano FROM higiene_mascota WHERE ultimoBano IS NOT NULL ORDER BY fecha DESC LIMIT 1")
    suspend fun obtenerUltimoBano(): String?

    @Query("SELECT ultimoCorteUnas FROM higiene_mascota WHERE ultimoCorteUnas IS NOT NULL ORDER BY fecha DESC LIMIT 1")
    suspend fun obtenerUltimoCorteUnas(): String?
}