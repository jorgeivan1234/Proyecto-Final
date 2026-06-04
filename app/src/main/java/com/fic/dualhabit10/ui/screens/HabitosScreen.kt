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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// Lógica de Datos (ViewModel)
class HabitosViewModel(application: Application) : AndroidViewModel(application) {
    // Memoria local del dispositivo para guardar el progreso sin necesidad de base de datos compleja
    private val prefs = application.getSharedPreferences("dualhabit_prefs", Context.MODE_PRIVATE)

    // Estados que la interfaz gráfica vigila que llegan a cambiar la pantalla se actualiza sola
    var rachaContador by mutableStateOf(0)
        private set
    var diasSemanaActual by mutableStateOf<List<DiaData>>(emptyList())
        private set
    var rachaRegistradaHoy by mutableStateOf(false)
        private set
    var diasCompletados by mutableStateOf(setOf<String>())
        private set

    init {
        // Al iniciar la pantalla, calculamos cuántos días seguidos lleva el usuario
        cargarYVerificarRacha()
        generarDiasDeLaSemana()
    }

    private fun cargarYVerificarRacha() {
        try {
            val hoy = LocalDate.now()

            // Recuperamos los registros anteriores desde la memoria del dispositivo
            val rachaGuardada = prefs.getInt("racha_contador", 0)
            val ultimaFechaStr = prefs.getString("ultima_fecha_registro", null)
            val diasCompletadosGuardados = prefs.getStringSet("dias_completados_set", emptySet()) ?: emptySet()
            diasCompletados = diasCompletadosGuardados

            if (ultimaFechaStr == null) {
                // Es un usuario completamente nuevo que apenas instaló la aplicación
                rachaContador = 0
                rachaRegistradaHoy = false
            } else {
                val ultimaFecha = LocalDate.parse(ultimaFechaStr)

                // Calculamos cuántos días han pasado desde la última vez que registró su hábito
                val diasDeDiferencia = ChronoUnit.DAYS.between(ultimaFecha, hoy)
                when {
                    diasDeDiferencia == 0L -> {
                        // El usuario ya registró su progreso el día de hoy
                        rachaContador = rachaGuardada
                        rachaRegistradaHoy = true
                    }
                    diasDeDiferencia == 1L -> {
                        // Es un día nuevo, mantenemos el acumulado anterior pero habilitamos el botón
                        rachaContador = rachaGuardada
                        rachaRegistradaHoy = false
                    }
                    else -> {
                        // Han pasado dos días o más, provocando que se pierda la racha
                        rachaContador = 0
                        rachaRegistradaHoy = false
                        prefs.edit().putInt("racha_contador", 0).apply()
                    }
                }
            }
        } catch (e: Exception) {
            // En caso de cualquier error con las fechas, reiniciamos el contador por seguridad
            rachaContador = 0
            rachaRegistradaHoy = false
        }
    }

    fun registrarRachasDeHoy() {
        if (!rachaRegistradaHoy) {
            val hoy = LocalDate.now()
            val hoyFechaCompletaStr = hoy.toString()

            // Sumamos un día al acumulado y bloqueamos el botón para evitar registros dobles
            rachaContador += 1
            rachaRegistradaHoy = true
            diasCompletados = diasCompletados + hoyFechaCompletaStr

            // Guardamos la información actualizada en la memoria física del teléfono
            prefs.edit()
                .putInt("racha_contador", rachaContador)
                .putString("ultima_fecha_registro", hoy.toString())
                .putStringSet("dias_completados_set", diasCompletados)
                .apply()

            // Refrescamos los días en pantalla para que aparezca la palomita (check) en el día actual
            generarDiasDeLaSemana()
        }
    }

    fun generarDiasDeLaSemana() {
        val hoy = LocalDate.now()
        // Aseguramos que la semana siempre comience desde el día Lunes
        val lunesDeEstaSemana = hoy.with(DayOfWeek.MONDAY)
        val lista = mutableListOf<DiaData>()

        for (i in 0..6) {
            val fechaDia = lunesDeEstaSemana.plusDays(i.toLong())
            val fechaDiaCompletaStr = fechaDia.toString()

            // Obtenemos las tres primeras letras del día
            val nombreCorto = fechaDia.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale("es", "MX")
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
    val colorFondo: Color
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

    // Lista de tarjetas principales que dirigen a las diferentes secciones de la aplicación
    val listaHabitos = listOf(
        HabitoItem(
            titulo = stringResource(R.string.title_hidratacion), // Asegúrate de tener esto en strings.xml
            imagenRes = R.drawable.img_hidratacion,
            rutaNavigation = "hidratacion",
            colorFondo = Color(0xFFB3E5FC)
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_sueno),
            imagenRes = R.drawable.img_sueno,
            rutaNavigation = "sueño",
            colorFondo = Color(0xFF1A237E)
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_actividad_fisica),
            imagenRes = R.drawable.img_ejercicio,
            rutaNavigation = "actividad_fisica_mascota",
            colorFondo = Color(0xFFFFCDD2)
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_alimentacion),
            imagenRes = R.drawable.img_alimentacion,
            rutaNavigation = "alimentacion",
            colorFondo = Color(0xFFFFF9C4)
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_mascotas),
            imagenRes = R.drawable.img_mascotas_v,
            rutaNavigation = "mascota_menu",
            colorFondo = Color(0xFFD7CCC8)
        ),
        HabitoItem(
            titulo = stringResource(R.string.title_sugerencias),
            imagenRes = R.drawable.img_perro_comentario,
            rutaNavigation = "Sugerencias",
            colorFondo = Color(0xFFEE1BEE)
        )
    )

    // Contenedor principal que permite deslizar el menú lateral
    BaseCustomDrawer(navController = navController, drawerState = drawerState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFFF7A22),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 28.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // Botón para abrir el menú lateral
                    IconButton(
                        onClick = { scoper.launch { drawerState.open() } },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.desc_menu),
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Botón central con forma de píldora que registra la racha del usuario
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                            .clickable(enabled = !viewModel.rachaRegistradaHoy) {
                                viewModel.registrarRachasDeHoy()
                            }
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .align(Alignment.Center)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (viewModel.rachaRegistradaHoy) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.racha_lista),
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.activar_racha, viewModel.rachaContador),
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fila deslizable que muestra los días de la semana en la cabecera
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
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

            // Cuadrícula que organiza automáticamente las tarjetas de hábitos en dos columnas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
            .width(62.dp)
            .background(
                color = if (esHoy) Color(0xFFFFF200) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = diaSemana,
            color = if (esHoy) Color.Black else Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = numeroDia,
            color = if (esHoy) Color.Black else Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Indicador visual de éxito: Muestra una palomita si el usuario cumplió en ese día
        if (esCompletado) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(if (esHoy) Color(0xFF2E7D32) else Color(0xFFFFF200), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = if (esHoy) Color.White else Color.Black,
                    modifier = Modifier.size(12.dp)
                )
            }
        } else {
            // Mantiene el tamaño uniforme en caso de que el día no esté completado
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun Tarjetahabito(habito: HabitoItem, onClick: () -> Unit) {
    // Determina el color del texto dependiendo del fondo de la tarjeta para garantizar visibilidad
    val colorText = if (habito.colorFondo == Color(0xFF1A237E)) Color.White else Color.Black

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = habito.colorFondo
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = habito.titulo,
                color = colorText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Image(
                painter = painterResource(id = habito.imagenRes),
                contentDescription = habito.titulo,
                modifier = Modifier
                    .size(85.dp)
                    .padding(bottom = 4.dp)
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