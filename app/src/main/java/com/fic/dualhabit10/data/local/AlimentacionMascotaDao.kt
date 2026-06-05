package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlimentacionMascotaDao{
    @Query("SELECT * FROM tabla_recetas_mascota")
    fun obtenerRecetasMascota(): Flow<List<AlimentacionMascotaEntity>>

    @Query("SELECT * FROM tabla_recetas_mascota WHERE id = :recetaId LIMIT 1")
    fun buscarRecetaPorId(recetaId: Int): Flow<AlimentacionMascotaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRecetasMascota(recetas: List<AlimentacionMascotaEntity>)

    @Query("SELECT COUNT(*) FROM tabla_recetas_mascota")
    suspend fun cantidadRecetasMascota(): Int
}