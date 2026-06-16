package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadFisicaDao {

    //obtiene todas las actividades guardadas en la bd
    @Query("SELECT * FROM actividades_fiscias ORDER BY id ASC")
    fun obtenerActividades(): Flow<List<ActividadFisicaEntity>>

    //inserta una lista completa dee actividades
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarActividades(actividades: List<ActividadFisicaEntity>)

    @Query("SELECT * FROM actividades_fiscias WHERE id = :id LIMIT 1")
    fun obtenerActividadPorId(id: Int): Flow<ActividadFisicaEntity?>

    //aun no sabemos si usarlo pero hce que el usuario agrege un ejercicio personalizado
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUnaActividad(actividad: ActividadFisicaEntity)*/

    //comprueba si la bse de dtos yaa tiene ejercicios precargados
    @Query("SELECT COUNT(*) FROM actividades_fiscias")
    suspend fun contarActividades(): Int
}
