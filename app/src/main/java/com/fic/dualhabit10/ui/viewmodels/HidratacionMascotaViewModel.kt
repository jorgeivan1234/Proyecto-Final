
package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.RegistroAguaMascotaEntity
import com.fic.dualhabit10.data.remote.WeatherApiService
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
    private val perfilDao = database.perfilMacotaDao()
    private val weatherApi = WeatherApiService.crear()
    //fecha de hoy
    private val hoyStr = LocalDate.now().toString()

    var aguaMascotaConsumidaML by mutableIntStateOf(0)
        private  set
    var metaMascotaDiariaML by mutableIntStateOf(500)
        private  set
    var tipoMascota by mutableStateOf("Perro")
    var pesoKG by mutableStateOf("10")
    var tipoDieta by mutableStateOf("seco")
    var climaCaluroso by mutableStateOf(false)
    var hizoEjercicio by mutableStateOf(false)

    var temperaturaActual  by mutableFloatStateOf(30.0f)
        private set

    init{
        cargarDatosHoyYClima()
    }

    private fun cargarDatosHoyYClima() {
        viewModelScope.launch {

            //lee el perfil local de room interno
            perfilDao.obtenerPerfil().collect { perfil ->
                if (perfil != null) {
                    tipoMascota = perfil.especie
                    pesoKG = perfil.peso.toString()

                }

                launch {
                    //consulta el clima
                    try {
                        val respuesta = weatherApi.ObtenerClimaActual(latitude = 24.8053, longitude = -107.3943)
                        temperaturaActual = respuesta.current.temperature_2m
                        climaCaluroso = temperaturaActual >= 30.0f
                    } catch (e: Exception) {
                        climaCaluroso = true
                    }
                    //carga el progreso del agua tomada por hoy
                    val registro = mascotaDao.obtenerRegistroMascotaPorFecha(hoyStr)
                    if (registro != null) {
                        aguaMascotaConsumidaML = registro.cantidadML
                        metaMascotaDiariaML = registro.metaML
                    } else {
                        calcularMetaInteligente()
                    }
                }
            }
        }
    }

    fun calcularMetaInteligente(){
        val peso = pesoKG.toFloatOrNull() ?: 10f
        //formula base de veterinaria mas o menos
        var baseML = when (tipoMascota) {
            "Perro" -> peso * 60f
            "Gato" -> peso * 45f
            "Ave" -> peso * 8f
            "Roedor/Conejo" -> peso * 70f
            else -> peso * 50f
        }

        if (tipoDieta == "Humedo") baseML *= 0.7f
        if (climaCaluroso) baseML *= 1.4f
        if (hizoEjercicio) baseML *= 1.2f

        metaMascotaDiariaML = baseML.toInt().coerceAtLeast(100)
        guardarEstadoActual()
    }

    fun sumarAguaMascota(ml: Int) {
        aguaMascotaConsumidaML += ml
        guardarEstadoActual()
    }
    fun reiniciarProgreso() {
        aguaMascotaConsumidaML = 0
        guardarEstadoActual()
    }
    private fun guardarEstadoActual(){
        viewModelScope.launch {
            val nuevoRegistro = RegistroAguaMascotaEntity(
                fecha = hoyStr,
                cantidadML = aguaMascotaConsumidaML,
                metaML = metaMascotaDiariaML
            )
            mascotaDao.insertarOActualizarMascota(nuevoRegistro)
        }
    }
    fun guardarPerfilMascota(
        dieta: String,
        ejercicio: Boolean,
        climaManualCaluroso: Boolean
    ) {
        tipoDieta = dieta
        hizoEjercicio = ejercicio
        climaCaluroso = climaManualCaluroso
        calcularMetaInteligente()
    }
}