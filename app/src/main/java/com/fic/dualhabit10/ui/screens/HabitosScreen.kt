package com.fic.dualhabit10.ui.screens

import android.app.Application
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulOscuroSueno
import com.fic.dualhabit10.ui.theme.RosaActividad
import com.fic.dualhabit10.ui.theme.AmarilloAlimentacion
import com.fic.dualhabit10.ui.theme.CafeMascotas
import com.fic.dualhabit10.ui.theme.MagentaSugerencias
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.AzulHidratacion
import com.fic.dualhabit10.ui.theme.VerdeCompletado
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco

// Lógica de Datos (ViewModel)
class HabitosViewModel(application: Application) : AndroidViewModel(application) {

    // Constantes para evitar typos en las llaves de memoria
    companion object {
        private const val PREFS_NAME = "dualhabit_prefs"
        private const val KEY_RACHA_CONTADOR = "racha_contador"
        private const val KEY_ULTIMA_FECHA = "ultima_fecha_registro"
        private const val KEY_DIAS_COMPLETADOS = "dias_completados_set"
    }

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var rachaContador by mutableStateOf(0)
        private set
    var diasSemanaActual by mutableStateOf<List<DiaData>>(emptyList())
        private set
    var rachaRegistradaHoy by mutableStateOf(false)
        private set
    var diasCompletados by mutableStateOf(setOf<String>())
        private set

    init {
        cargarYVerificarRacha()
        generarDiasDeLaSemana()
    }

    private fun cargarYVerificarRacha() {
        try {
            val hoy = LocalDate.now()
            val rachaGuardada = prefs.getInt(KEY_RACHA_CONTADOR, 0)
            val ultimaFechaStr = prefs.getString(KEY_ULTIMA_FECHA, null)
            val diasCompletadosGuardados = prefs.getStringSet(KEY_DIAS_COMPLETADOS, emptySet()) ?: emptySet()
            diasCompletados = diasCompletadosGuardados

            if (ultimaFechaStr == null) {
                rachaContador = 0
                rachaRegistradaHoy = false
            } else {
                val ultimaFecha = LocalDate.parse(ultimaFechaStr)
                val diasDeDiferencia = ChronoUnit.DAYS.between(ultimaFecha, hoy)

                when {
                    diasDeDiferencia == 0L -> {
                        rachaContador = rachaGuardada
                        rachaRegistradaHoy = true
                    }
                    diasDeDiferencia == 1L -> {
                        rachaContador = rachaGuardada
                        rachaRegistradaHoy = false
                    }
                    else -> {
                        rachaContador = 0
                        rachaRegistradaHoy = false
                        prefs.edit().putInt(KEY_RACHA_CONTADOR, 0).apply()
                    }
                }
            }
        } catch (e: Exception) {
            rachaContador = 0
            rachaRegistradaHoy = false
        }
    }

    fun registrarRachasDeHoy() {
        if (!rachaRegistradaHoy) {
            val hoy = LocalDate.now()
            val hoyFechaCompletaStr = hoy.toString()

            rachaContador += 1
            rachaRegistradaHoy = true
            diasCompletados = diasCompletados + hoyFechaCompletaStr

            prefs.edit()
                .putInt(KEY_RACHA_CONTADOR, rachaContador)
                .putString(KEY_ULTIMA_FECHA, hoy.toString())
                .putStringSet(KEY_DIAS_COMPLETADOS, diasCompletados)
                .apply()

            generarDiasDeLaSemana()
        }
    }

    fun generarDiasDeLaSemana() {
        val hoy = LocalDate.now()
        val lunesDeEstaSemana = hoy.with(DayOfWeek.MONDAY)
        val lista = mutableListOf<DiaData>()

        for (i in 0..6) {
            val fechaDia = lunesDeEstaSemana.plusDays(i.toLong())
            val fechaDiaCompletaStr = fechaDia.toString()

            val nombreCorto = fechaDia.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault()
            ).replaceFirstChar { it.uppercase() }

            lista.add(
                DiaData(
                    nombre = nombreCorto,
                    numero = fechaDia.dayOfMonth.toString(),
                    esHoy = fechaDia.isEqual(hoy),
                    estaCompletado = diasCompletados.contains(fechaDiaCompletaStr)
                )
            )
        }
        diasSemanaActual = lista
    }
}

// Modelos de Datos
data class DiaData(
    val nombre: String,
    val numero: String,
    val esHoy: Boolean,
    val estaCompletado: Boolean
)

data class HabitoItem(
    val titulo: String,
    val imagenRes: Int,
    val rutaNavigation: String,
    val colorFondo: Color,
    val colorTexto: Color
)

// Interfaz Gráfica (UI)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitosScreen(
    navController: NavController,
    viewModel: HabitosViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scoper = rememberCoroutineScope()

    val listaHabitos = listOf(
        HabitoItem(
            titulo = stringResource(R.string.title_hidratacion),
            imagenRes = R.drawable.img_hidratacion,
            rutaNavigation = "hidratacion",
            colorFondo = AzulHidratacion,
            colorTexto = TextoNegro
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_sueno),
            imagenRes = R.drawable.img_sueno,
            rutaNavigation = "sueño",
            colorFondo = AzulOscuroSueno,
            colorTexto = TextoBlanco
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_actividad_fisica),
            imagenRes = R.drawable.img_ejercicio,
            rutaNavigation = "actividad_fisica_mascota",
            colorFondo = RosaActividad,
            colorTexto = TextoNegro
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_alimentacion),
            imagenRes = R.drawable.img_alimentacion,
            rutaNavigation = "alimentacion",
            colorFondo = AmarilloAlimentacion,
            colorTexto = TextoNegro
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_mascotas),
            imagenRes = R.drawable.img_mascotas_v,
            rutaNavigation = "mascota_menu",
            colorFondo = CafeMascotas,
            colorTexto = TextoNegro
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_sugerencias),
            imagenRes = R.drawable.img_perro_comentario,
            rutaNavigation = "Sugerencias",
            colorFondo = MagentaSugerencias,
            colorTexto = TextoBlanco
        )
    )

    BaseCustomDrawer(navController = navController, drawerState = drawerState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = NaranjaCabecera,
                        // Utilizamos shape manual porque es asimétrico
                        shape = RoundedCornerShape(bottomStart = Dimens.paddingLarge, bottomEnd = Dimens.paddingLarge)
                    )
                    .statusBarsPadding()
                    .padding(
                        start = Dimens.paddingMedium,
                        end = Dimens.paddingMedium,
                        bottom = Dimens.paddingLarge,
                        top = Dimens.paddingExtraLarge
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.paddingTiny)
                ) {
                    IconButton(
                        onClick = { scoper.launch { drawerState.open() } },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.desc_menu),
                            tint = TextoNegro,
                            modifier = Modifier.size(Dimens.iconSizeLarge)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(AmarilloFondo, shape = MaterialTheme.shapes.extraLarge)
                            .clickable(enabled = !viewModel.rachaRegistradaHoy) {
                                viewModel.registrarRachasDeHoy()
                            }
                            .padding(horizontal = Dimens.paddingLarge, vertical = Dimens.paddingSmall)
                            .align(Alignment.Center)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (viewModel.rachaRegistradaHoy) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = stringResource(R.string.desc_racha_completada),
                                    tint = TextoNegro,
                                    modifier = Modifier.size(Dimens.iconSizeSmall)
                                )
                                Spacer(modifier = Modifier.width(Dimens.paddingTiny))
                                Text(
                                    text = stringResource(R.string.racha_lista),
                                    color = TextoNegro,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.activar_racha, viewModel.rachaContador),
                                    color = TextoNegro,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.paddingDefault))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    viewModel.diasSemanaActual.forEach { dia ->
                        DiaItem(
                            diaSemana = dia.nombre,
                            numeroDia = dia.numero,
                            esHoy = dia.esHoy,
                            esCompletado = dia.estaCompletado
                        )
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = Dimens.paddingDefault, vertical = Dimens.paddingMedium),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingDefault),
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault)
            ) {
                items(listaHabitos) { habito ->
                    Tarjetahabito(habito = habito, onClick = {
                        if (habito.rutaNavigation.isNotEmpty()) {
                            navController.navigate(habito.rutaNavigation)
                        }
                    })
                }
            }
        }
    }
}

// Componentes Visuales Reutilizables
@Composable
fun DiaItem(diaSemana: String, numeroDia: String, esHoy: Boolean, esCompletado: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(Dimens.diaItemWidth)
            .background(
                color = if (esHoy) AmarilloFondo else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = Dimens.paddingSmall)
    ) {
        Text(
            text = diaSemana,
            color = if (esHoy) TextoNegro else TextoBlanco,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = numeroDia,
            color = if (esHoy) TextoNegro else TextoBlanco,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Dimens.paddingTiny))

        if (esCompletado) {
            Box(
                modifier = Modifier
                    .size(Dimens.iconSizeSmall)
                    .background(if (esHoy) VerdeCompletado else AmarilloFondo, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.desc_dia_completado),
                    tint = if (esHoy) TextoBlanco else TextoNegro,
                    modifier = Modifier.size(Dimens.paddingMedium)
                )
            }
        } else {
            Spacer(modifier = Modifier.height(Dimens.iconSizeSmall))
        }
    }
}

@Composable
fun Tarjetahabito(habito: HabitoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.tarjetaHabitoHeight)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = habito.colorFondo
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = habito.titulo,
                color = habito.colorTexto,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(id = habito.imagenRes),
                contentDescription = habito.titulo,
                modifier = Modifier
                    .size(Dimens.imageHabitoSize)
                    .padding(bottom = Dimens.paddingTiny)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHabitos() {
    val nav = rememberNavController()
    HabitosScreen(navController = nav)
}