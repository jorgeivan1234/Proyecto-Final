package com.fic.dualhabit10.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.fic.dualhabit10.R

// Diccionarios en memoria
private val mapaStrings = mapOf(

    // Recetas para Mascotas
    "receta_croque_nombre" to R.string.receta_croque_nombre,
    "receta_croque_desc" to R.string.receta_croque_desc,
    "receta_croque_cal" to R.string.receta_croque_cal,
    "receta_croque_tipo" to R.string.receta_croque_tipo,

    "receta_fresa_nombre" to R.string.receta_fresa_nombre,
    "receta_fresa_desc" to R.string.receta_fresa_desc,
    "receta_fresa_cal" to R.string.receta_fresa_cal,
    "receta_fresa_tipo" to R.string.receta_fresa_tipo,

    "receta_avena_nombre" to R.string.receta_avena_nombre,
    "receta_avena_desc" to R.string.receta_avena_desc,
    "receta_avena_cal" to R.string.receta_avena_cal,
    "receta_avena_tipo" to R.string.receta_avena_tipo,

    "receta_caldo_nombre" to R.string.receta_caldo_nombre,
    "receta_caldo_desc" to R.string.receta_caldo_desc,
    "receta_caldo_cal" to R.string.receta_caldo_cal,
    "receta_caldo_tipo" to R.string.receta_caldo_tipo,

    // Recetas para Humanos
    "receta_pollo_nombre" to R.string.receta_pollo_nombre,
    "receta_pollo_desc" to R.string.receta_pollo_desc,
    "receta_salmon_nombre" to R.string.receta_salmon_nombre,
    "receta_salmon_desc" to R.string.receta_salmon_desc,

    // Actividad física
    "canicross_ligero_titulo" to R.string.canicross_ligero_titulo,
    "canicross_ligero_desc" to R.string.canicross_ligero_desc,

    "agility_casero_titulo" to R.string.agility_casero_titulo,
    "agility_casero_desc" to R.string.agility_casero_desc,

    "caminata_intervalos_titulo" to R.string.caminata_intervalos_titulo,
    "caminata_intervalos_desc" to R.string.caminata_intervalos_desc,

    "lanzamiento_despliegue_titulo" to R.string.lanzamiento_despliegue_titulo,
    "lanzamiento_despliegue_desc" to R.string.lanzamiento_despliegue_desc,

    "yoga_dogi_titulo" to R.string.yoga_dogi_titulo,
    "yoga_dogi_desc" to R.string.yoga_dogi_desc,

    "frisbee_cardio_titulo" to R.string.frisbee_cardio_titulo,
    "frisbee_cardio_desc" to R.string.frisbee_cardio_desc,

    // Reutilizables (Duración e Intensidad)
    "duracion_15m" to R.string.duracion_15m,
    "duracion_20m" to R.string.duracion_20m,
    "duracion_25m" to R.string.duracion_25m,
    "duracion_30m" to R.string.duracion_30m,
    "duracion_40m" to R.string.duracion_40m,

    "intensidad_baja" to R.string.intensidad_baja,
    "intensidad_media" to R.string.intensidad_media,
    "intensidad_alta" to R.string.intensidad_alta,
    "intensidad_baja_media" to R.string.intensidad_baja_media,

    // Sistema
    "nota_juego_completado" to R.string.nota_juego_completado
)

// Mapeado de Arrays para recetas de Humanos
private val mapaArrays = mapOf(
    "receta_pollo_ingredientes" to R.array.receta_pollo_ingredientes,
    "receta_pollo_pasos" to R.array.receta_pollo_pasos,
    "receta_salmon_ingredientes" to R.array.receta_salmon_ingredientes,
    "receta_salmon_pasos" to R.array.receta_salmon_pasos
)

@Composable
fun traducirTexto(clave: String): String {
    val resId = mapaStrings[clave]
    return if (resId != null) stringResource(id = resId) else clave
}

@Composable
fun traducirLista(lista: List<String>): List<String> {
    if (lista.size == 1) {
        val resId = mapaArrays[lista[0]]
        if (resId != null) {
            return stringArrayResource(id = resId).toList()
        }
    }
    return lista
}