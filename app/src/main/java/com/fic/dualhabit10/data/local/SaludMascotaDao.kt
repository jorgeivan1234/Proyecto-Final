package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SaludMascotaDao {
    @Query("SELECT * FROM salud_mascota ORDER BY id DESC")
    fun obtenerHistorialSalud(): Flow<List<SaludMascotaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRegistroSalud(registro: SaludMascotaEntity)

    @Delete
    suspend fun eliminarRegistroSalud(registro: SaludMascotaEntity)
}
