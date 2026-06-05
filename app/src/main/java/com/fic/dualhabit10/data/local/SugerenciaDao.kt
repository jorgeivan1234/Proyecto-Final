package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SugerenciaDao {

    @Insert
    suspend fun insertarSugerencia(sugerencia: SugerenciaEntity)

    @Query("SELECT * FROM sugerencias ORDER BY id DESC")
    fun obtenerSugerencias(): Flow<List<SugerenciaEntity>>
}