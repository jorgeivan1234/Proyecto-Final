package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.RegistroAguaEntity
import com.fic.dualhabit10.data.remote.WeatherApiService
import kotlinx.coroutines.launch
import java.time.LocalDate


class HidratacionViewModel (application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("dualhabit_prefs", Context.MODE_PRIVATE)

    private val database = AppDatabase.getDatebase(application)
    private val dnHidratacionDao = database.hidratacionDao()
    private val weatherApiService = WeatherApiService.crear()
    private val hoyStr = LocalDate.now().toString()
    var aguaConsumidaML by mutableIntStateOf(0)
        private set
    var metaDiariaML by mutableIntStateOf(2000)
        private set

    //estaddos del perfil compartido
    var usuarioPeso by mutableFloatStateOf(70f)
    var usuarioEdad by mutableStateOf(25)
    var usuarioGenero by mutableStateOf("Masculino")
    var actividadNivel by mutableStateOf("Moderado")
    var entornoClima by mutableStateOf("Calido")
    var experienciaNivel by mutableIntStateOf(1)
    var experienciaPuntos by mutableIntStateOf(0)

    // variabilidad de lectura para pintar los grados
    var temperaturaActual by mutableFloatStateOf(30.0f)
        private set
    //almacena el historial en la base de datos
    val historialConsumoMap = mutableStateMapOf<String, Int>()

    init {
        cargarDatosLocales()
        cargarAguaDesdeRoom()
        cargarHistorialDesdeRoom()
        obtenerClimaDesdeApi()
    }

    private fun obtenerClimaDesdeApi(){
        viewModelScope.launch {
            try {
                //la cordenada de aqui
                val respuesta = weatherApiService.ObtenerClimaActual(
                    latitude = 24.8053,
                    longitude = -107.3943
                )
                temperaturaActual = respuesta.current.temperature_2m
                entornoClima = if (temperaturaActual >= 30.0f) "Calido" else "Frio"
            } catch (e: Exception) {
                //si no hay red conserva el valor por defecto de SharedPreferences
            } finally {
                calcularMetaHidratacionDinamica()
            }
        }
    }
    private fun cargarAguaDesdeRoom() {
        viewModelScope.launch {
            val registroHoy = dnHidratacionDao.obtenerRegistroPorFecha(hoyStr)
            if (registroHoy != null) {
                aguaConsumidaML = registroHoy.cantidadML
            } else {
                aguaConsumidaML = 0
                // si es un dia nuevo guardamos un registro en cero
                dnHidratacionDao.insertarOActualizar(
                    RegistroAguaEntity(fecha = hoyStr, cantidadML = 0, metaML = metaDiariaML)
                )
            }
        }
    }

    private fun cargarHistorialDesdeRoom() {
        viewModelScope.launch {
            dnHidratacionDao.obtenerTodoElHistorial().collect { listaEntiedades ->
                historialConsumoMap.clear()
                for (registro in listaEntiedades) {
                    historialConsumoMap[registro.fecha] = registro.cantidadML
                }
            }
        }
    }

    private fun cargarDatosLocales() {
        //cargara los parametros de perfil
        usuarioPeso = prefs.getFloat("perfil_peso", 70f)
        usuarioEdad = prefs.getInt("perfil_edad", 25)
        usuarioGenero = prefs.getString("perfil_genero", "Masculino") ?: "Masculino"
        actividadNivel = prefs.getString("perfil_actividad", "Moderado") ?: "Moderado"
        entornoClima = prefs.getString("perfil_entorno", "Calido") ?: "Calido"

        //carga gamificacion
        experienciaNivel = prefs.getInt("gamificacion_nivel", 1)
        experienciaPuntos = prefs.getInt("gamificacion_xp", 0)
    }

    //calculo automatico de meta basado en el perfil y ajuste metereologico dinamico
    fun calcularMetaHidratacionDinamica() {
        var baseMeta = (usuarioPeso * 35f).toInt()

        //ajuste por nivel de actividad fisica
        if (actividadNivel == "Alto" || actividadNivel == "Intenso") {
            baseMeta += 600
        }

        if (entornoClima == "Calido" || entornoClima == "Extremo") {
            baseMeta += 500
        }
        metaDiariaML = baseMeta
    }

    //registro de incrementeo de exp
    fun agregarAgua(ml: Int) {
        aguaConsumidaML += ml

        viewModelScope.launch {
            //sinconizacion con la base de datos
            val nuevoRegistro = RegistroAguaEntity(
                fecha = hoyStr,
                cantidadML = aguaConsumidaML,
                metaML = metaDiariaML,
            )
            dnHidratacionDao.insertarOActualizar(nuevoRegistro)
        }
        //sistema de recompensa
        experienciaPuntos += (ml / 10)
        if (experienciaPuntos >= 100 * experienciaNivel) {
            experienciaPuntos = 0
            experienciaNivel += 1
            prefs.edit().putInt("gamificacion_nivel", experienciaNivel).apply()
        }
        prefs.edit().putInt("gamificacion_xp", experienciaPuntos).apply()
    }

    fun guardarPerfil(peso: Float, edad: Int, genero: String, actividad: String) {
        usuarioPeso = peso
        usuarioEdad = edad
        usuarioGenero = genero
        actividadNivel = actividad

        prefs.edit()
            .putFloat("perfil_peso", peso)
            .putInt("perfil_edad", edad)
            .putString("perfil_genero", genero)
            .putString("perfil_actividad", actividad)
            .putString("perfil_entorno", entornoClima)
            .apply()
        calcularMetaHidratacionDinamica()

        viewModelScope.launch {
            dnHidratacionDao.insertarOActualizar(
                RegistroAguaEntity(
                    fecha = hoyStr,
                    cantidadML = aguaConsumidaML,
                    metaML = metaDiariaML
                )
            )
        }
    }
}