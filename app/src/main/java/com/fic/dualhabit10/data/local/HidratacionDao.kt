package com.fic.dualhabit10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fic.dualhabit10.data.local.RegistroAguaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HidratacionDao {
    //inserta o actualiza elregistro del dia, si ya existe la fecha la remplaza con el nuevo conteo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOActualizar(registro: RegistroAguaEntity)
    //obtiene el consumo de un dia especifico
    @Query("SELECT * FROM historial_agua WHERE fecha= :fecha LIMIT 1")
    suspend fun obtenerRegistroPorFecha(fecha: String): RegistroAguaEntity?

    @Query("SELECT * FROM historial_agua ORDER BY fecha DESC")
    fun obtenerTodoElHistorial(): Flow<List<RegistroAguaEntity>>
}