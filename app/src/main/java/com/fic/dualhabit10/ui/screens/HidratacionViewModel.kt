package com.fic.dualhabit10.ui.screens

import android.app.Application
import android.content.Context
import androidx.collection.mutableFloatSetOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import java.time.LocalDate

class HidratacionViewModel (application: Application) : AndroidViewModel(application){
    private val prefs = application.getSharedPreferences("dualhabit_prefs", Context.MODE_PRIVATE)

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

    init{
        cargarDatosLocales()
        calcularMetaHidratacionDinamica()
    }

    private fun cargarDatosLocales(){
        val hoyStr = LocalDate.now().toString()
        val fechaGuardadaAgua = prefs.getString("fecha_agua", null)
        //carga o reinicio diario del agua
        if (fechaGuardadaAgua == hoyStr){
            aguaConsumidaML = prefs.getInt("agua_consumida_real", 0)
        } else {
            aguaConsumidaML = 0
            prefs.edit().putInt("agua_consumida_real", 0).putString("fecha_agua", hoyStr).apply()
        }

        //cargara los parametros de perfil
        usuarioPeso = prefs.getFloat("perfil_peso", 70f)
        usuarioEdad = prefs.getInt("perfil_edad", 25)
        usuarioGenero = prefs.getString("perfil_genero", "Masculino") ?: "Masculino"
        actividadNivel = prefs.getString("perfil_actividad", "Moderado") ?: "Moderado"
        entornoClima = prefs.getString("perfil_entorno", "Calido") ?: "Calido"

        //carga gamificacion
        experienciaNivel = prefs.getInt("gamificacion_nivel",1)
        experienciaPuntos = prefs.getInt("gamificacion_xp",0)
    }
    //calculo automatico de meta basado en el perfil y ajuste metereologico dinamico
    fun calcularMetaHidratacionDinamica(){
        var baseMeta = (usuarioPeso * 35f).toInt()

        //ajuste por nivel de actividad fisica
        if(actividadNivel == "Alto" || actividadNivel == "Intenso"){
            baseMeta += 600
        }

        if(entornoClima == "Calido" || entornoClima == "Extremo"){
            baseMeta += 500
        }
        metaDiariaML = baseMeta
    }
    //registro de incrementeo de exp
    fun agregarAgua(ml: Int){
        aguaConsumidaML += ml
        prefs.edit().putInt("agua_consumida_real", aguaConsumidaML).apply()

        //sistema de recompensa
        experienciaPuntos += (ml/10)
        if(experienciaPuntos >= 100 * experienciaNivel){
            experienciaPuntos = 0
            experienciaNivel += 1
            prefs.edit().putInt("gamificacion_nivel", experienciaNivel).apply()
        }
        prefs.edit().putInt("gamificacion_xp", experienciaPuntos).apply()
    }
    fun guardarPerfil(peso: Float, edad: Int, genero:String, actividad: String, entorno: String){
        usuarioPeso = peso
        usuarioEdad = edad
        usuarioGenero = genero
        actividadNivel = actividad
        entornoClima = entorno

        prefs.edit()
            .putFloat("perfil_peso", peso)
            .putInt("perfil_edad", edad)
            .putString("perfil_genero", genero)
            .putString("perfil_actividad", actividad)
            .putString("perfil_entorno", entorno)
            .apply()
        calcularMetaHidratacionDinamica()
    }
}
