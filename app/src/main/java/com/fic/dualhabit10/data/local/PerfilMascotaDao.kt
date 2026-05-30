package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilMascotaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPerfil(perfil: PerfilMascotaEntity)

    @Query("SELECT * FROM perfil_mascota WHERE id = 1 LIMIT 1")
    fun obtenerPerfil(): Flow<PerfilMascotaEntity?>
}
