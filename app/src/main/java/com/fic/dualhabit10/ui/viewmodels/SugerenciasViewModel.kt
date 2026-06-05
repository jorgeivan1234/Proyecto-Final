package com.fic.dualhabit10.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fic.dualhabit10.data.local.AppDatabase
import com.fic.dualhabit10.data.local.SugerenciaEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SugerenciasViewModel (application: Application) : AndroidViewModel(application) {

    private val sugerenciaDao = AppDatabase.getDatebase(application).sugerenciaDao()
    fun guardarSugerencias(texto: String) {
        viewModelScope.launch {
            val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val nuevaSugerencia = SugerenciaEntity(
                mensaje = texto,
                fecha = fechaActual
            )
            sugerenciaDao.insertarSugerencia(nuevaSugerencia)
            Log.d("SugerenciasDev", "¡Sugerencia guardada localmente!: ${nuevaSugerencia}")
        }
    }
}