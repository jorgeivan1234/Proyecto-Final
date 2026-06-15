package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AlimentacionMascotaEntity
import com.fic.dualhabit10.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlimentacionMascotaViewModel(application: Application) : AndroidViewModel(application) {

    private val mascotaDao = AppDatabase.getDatebase(application).alimentacionMascotaDao()
    private val _recetas = MutableStateFlow<List<AlimentacionMascotaEntity>>(emptyList())
    val recetas: StateFlow<List<AlimentacionMascotaEntity>> = _recetas

    init {
        checarYPrecargarRecetas()
        escucharRecetasLocales()
    }

    // Se mantiene esta función porque es necesaria para ver los detalles de una receta específica
    fun buscarRecetaPorId(recetaId: Int): Flow<AlimentacionMascotaEntity?> {
        return mascotaDao.buscarRecetaPorId(recetaId)
    }

    private fun escucharRecetasLocales() {
        viewModelScope.launch {
            mascotaDao.obtenerRecetasMascota().collectLatest { entidades ->
                _recetas.value = entidades
            }
        }
    }

    private fun checarYPrecargarRecetas() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mascotaDao.cantidadRecetasMascota() == 0) {
                // Solo insertamos las claves de nuestro diccionario para traducción dinámica
                val recetasDefecto = listOf(
                    AlimentacionMascotaEntity(
                        nombre = "receta_croque_nombre",
                        descripcion = "receta_croque_desc",
                        calorias = "receta_croque_cal",
                        TipoMascota = "receta_croque_tipo",
                        imagenUrl = "croquetas_caseras",
                        colorHex = "#FFFDE7"
                    ),
                    AlimentacionMascotaEntity(
                        nombre = "receta_fresa_nombre",
                        descripcion = "receta_fresa_desc",
                        calorias = "receta_fresa_cal",
                        TipoMascota = "receta_fresa_tipo",
                        imagenUrl = "helados_fresa",
                        colorHex = "#FCE4EC"
                    ),
                    AlimentacionMascotaEntity(
                        nombre = "receta_avena_nombre",
                        descripcion = "receta_avena_desc",
                        calorias = "receta_avena_cal",
                        TipoMascota = "receta_avena_tipo",
                        imagenUrl = "galletas_avena",
                        colorHex = "#F5F5F5"
                    ),
                    AlimentacionMascotaEntity(
                        nombre = "receta_caldo_nombre",
                        descripcion = "receta_caldo_desc",
                        calorias = "receta_caldo_cal",
                        TipoMascota = "receta_caldo_tipo",
                        imagenUrl = "caldo_huesos",
                        colorHex = "#FFE0B2"
                    )
                )
                mascotaDao.insertarRecetasMascota(recetasDefecto)
            }
        }
    }
}