package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PaseoDao {
    @Query("SELECT * FROM registro_paseos ORDER BY id DESC")
    fun obtenerTodosLosPaseos(): Flow<List<PaseoEntity>>

    @Insert
    suspend fun insertarPaseo(paseos: PaseoEntity)

    @Delete
    suspend fun eliminarPaseo(paseos: PaseoEntity)
}