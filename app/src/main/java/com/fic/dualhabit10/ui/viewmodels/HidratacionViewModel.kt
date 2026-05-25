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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate


class HidratacionViewModel (application: Application) : AndroidViewModel(application){
    private val prefs = application.getSharedPreferences("dualhabit_prefs", Context.MODE_PRIVATE)
    private val db = FirebaseFirestore.getInstance()
    private val usuarioId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: "usuario_anonimo"

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

    val hoyStr = LocalDate.now().toString()

    //almacena el historial en la base de datos
    val historialConsumoMap = mutableStateMapOf<String, Int>()
    init{
        cargarDatosLocales()
        calcularMetaHidratacionDinamica()
        subirMetaFirebase()
        escucharHistoriaFirebase()
    }

    private fun cargarDatosLocales(){
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

        //sinconizacion con la base de datos
        val datosRegistro = hashMapOf(
            "cantidadML" to aguaConsumidaML,
            "metaML" to metaDiariaML,
            "fecha" to hoyStr
        )
        db.collection("usuarios").document(usuarioId)
            .collection("historial_agua").document(hoyStr)
            .set(datosRegistro)

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
        subirMetaFirebase()
    }
    private fun subirMetaFirebase() {
        val datosPerfil = hashMapOf(
            "peso" to usuarioPeso,
            "actividadNivel" to actividadNivel,
            "entornoClima" to entornoClima,
            "metaDiariaML" to metaDiariaML
        )
        db.collection("usuarios").document(usuarioId)
            .set(datosPerfil, com.google.firebase.firestore.SetOptions.merge())
    }

    private fun escucharHistoriaFirebase() {
        db.collection("usuarios").document(usuarioId)
            .collection("historial_agua")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener

                historialConsumoMap.clear()
                for (doc in snapshot.documents) {
                    val fecha = doc.id
                    val cantidad = doc.getLong("cantidadML")?.toInt() ?: 0
                    historialConsumoMap[fecha] = cantidad
                }
            }
    }
}