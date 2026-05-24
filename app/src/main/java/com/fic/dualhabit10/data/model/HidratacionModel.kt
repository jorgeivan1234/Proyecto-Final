package com.fic.dualhabit10.data.model

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class RegistroAguFirebase(
    val cantidadML: Int = 0,
    val fechaHora: Timestamp = Timestamp.now(),
    val fechaDia: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
)

data class HistorialMetasFirebase(
    val metaCalcularML: Int = 0,
    val peso: Float = 0f,
    val actividad: String = "",
    val clima: String = "",
    val fechaRegistro: Timestamp = Timestamp.now()
)