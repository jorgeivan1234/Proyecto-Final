
package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.RegistroAguaMascotaEntity
import kotlinx.coroutines.launch
import java.time.LocalDate


data class RegistroAguaMascota(
    val fecha: String,
    val cantidadML: Int,
    val metaML: Int
)

class HidratacionMascotaViewModel(application: Application) : AndroidViewModel(application) {
    //conexion a room
    private val database = AppDatabase.getDatebase(application)
    private val mascotaDao = database.hidratacionMascotaDao()
    //fecha de hoy
    private val hoyStr = LocalDate.now().toString()

    var aguaMascotaConsumidaML by mutableIntStateOf(0)
        private  set
    var metaMascotaDiariaML by mutableIntStateOf(500)
        private  set

    init {
        cargarDatosHoy()
    }
    private fun cargarDatosHoy() {
        viewModelScope.launch {
            val registro = mascotaDao.obtenerRegistroMascotaPorFecha(hoyStr)
            if (registro != null) {
                aguaMascotaConsumidaML = registro.cantidadML
                metaMascotaDiariaML = registro.metaML
            } else {
                aguaMascotaConsumidaML = 0
                // si no hay registro hoy creamos un local inicial en 0
                mascotaDao.insertarOActualizarMascota(
                    RegistroAguaMascotaEntity(hoyStr, 0, metaMascotaDiariaML)
                )
            }
        }
    }
    //funcion que llama los botones de la pantalla del perro
    fun sumarAguaMascota(ml: Int) {
        aguaMascotaConsumidaML += ml
        viewModelScope.launch{
            val nuevoRegistro = RegistroAguaMascotaEntity(
                fecha = hoyStr,
                cantidadML = aguaMascotaConsumidaML,
                metaML = metaMascotaDiariaML
            )
            mascotaDao.insertarOActualizarMascota(nuevoRegistro)
        }
    }

    fun actualizarMetaMascota(nuevaMeta: Int) {
        metaMascotaDiariaML = nuevaMeta
        viewModelScope.launch{
            val nuevoRegistro = RegistroAguaMascotaEntity(
                fecha = hoyStr,
                cantidadML = aguaMascotaConsumidaML,
                metaML = metaMascotaDiariaML
            )
            mascotaDao.insertarOActualizarMascota(nuevoRegistro)
        }
    }

    // Nueva función para reiniciar el progreso
    fun reiniciarProgreso() {
        aguaMascotaConsumidaML = 0
        viewModelScope.launch {
            val nuevoRegistro = RegistroAguaMascotaEntity(
                fecha = hoyStr,
                cantidadML = 0,
                metaML = metaMascotaDiariaML
            )
            mascotaDao.insertarOActualizarMascota(nuevoRegistro)
        }
    }
}