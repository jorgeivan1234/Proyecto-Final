package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlimentacionDao {
    @Query("SELECT * FROM tabla_recetas")
    fun obtenerRecetas(): Flow<List<AlimentacionEntity>>

    @Query("SELECT * FROM tabla_recetas WHERE id = :recetaId LIMIT 1")
    suspend fun buscarRecetaPorId(recetaId: Int): AlimentacionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReceta(receta: List<AlimentacionEntity>)

    @Query("SELECT COUNT(*) FROM tabla_recetas")
    suspend fun cantidadRecetas(): Int
}
