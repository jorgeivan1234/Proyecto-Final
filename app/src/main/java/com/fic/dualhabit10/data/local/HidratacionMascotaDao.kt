package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HidratacionMascotaDao{
    //inserta el agua del perro o actualiza el registro si ya existe la fecha de hoy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOActualizarMascota(registro: RegistroAguaMascotaEntity)

    //busca cuanto ha tomado el perro en una fecha especifica
    @Query("SELECT * FROM historial_agua_mascota WHERE fecha = :fecha LIMIT 1")
    suspend fun obtenerRegistroMascotaPorFecha(fecha: String): RegistroAguaMascotaEntity?

    @Query("SELECT * FROM historial_agua_mascota ORDER BY fecha DESC")
    fun obtenerTodoElHistorialMascota(): Flow<List<RegistroAguaMascotaEntity>>
}