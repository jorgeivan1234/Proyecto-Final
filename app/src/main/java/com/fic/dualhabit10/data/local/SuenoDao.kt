package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SuenoDao {
    @Query("SELECT * FROM registro_sueno ORDER BY id DESC")
    fun obtenerHistorialSueno(): Flow<List<SuenoEntity>>

    @Insert
    suspend fun insertarSueno(sueno: SuenoEntity)

    @Query("DELETE FROM registro_sueno")
    suspend fun borrarTodo()
}