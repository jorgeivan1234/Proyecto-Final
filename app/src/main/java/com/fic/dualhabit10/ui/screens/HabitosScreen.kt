package com.fic.dualhabit10.ui.screens

import android.app.Application
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.AndroidViewModel
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

class HabitosViewModel (application: Application) : AndroidViewModel(application){
    private val prefs = application.getSharedPreferences("dualhabit_prefs", Context.MODE_PRIVATE)
    //estos son los estados que observa la ui
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
        val hoy = LocalDate.now()
        // recupera los datos guardados en la memoria
        val rachaGuardada = prefs.getInt("racha_contador", 0)
        val ultimaFechaStr = prefs.getString("ultima_fecha_registro", null)

        // recupera los dias del mes guardado que se completaron
        val diasCompletadosGuardados = prefs.getStringSet("dias_completados_set", emptySet()) ?: emptySet()
        diasCompletados = diasCompletadosGuardados

        if (ultimaFechaStr  == null) {
            //caso de usuario nuevo
            rachaContador = 0
            rachaRegistradaHoy = false
        } else {

            val ultimaFecha = LocalDate.parse(ultimaFechaStr)
            // para calcular la diferencia de dias reales entre la ultima vez que pulso el boton
            val diasDeDiferencia = ChronoUnit.DAYS.between(ultimaFecha, hoy)
            when {
                diasDeDiferencia == 0L -> {
                    //ya pulso el boton hiy
                    rachaContador = rachaGuardada
                    rachaRegistradaHoy = true
                }
                diasDeDiferencia == 1L -> {
                    //dia nuevo
                    rachaContador = rachaGuardada
                    rachaRegistradaHoy = false
                }
                else -> {
                    //pasron 2 dias o mas
                    rachaContador = 0
                    rachaRegistradaHoy = false
                    diasCompletados = emptySet()

                    //limpia la memoria
                    prefs.edit().putInt("racha_contador", 0)
                        .putStringSet("dias_completados_set", emptySet())
                        .apply()
                }
            }
        }
    }

    fun registrarRachasDeHoy() {
        if(!rachaRegistradaHoy) {
            val hoy = LocalDate.now()
            val hoyDiaStr = hoy.dayOfMonth.toString()

            //incremento el numero de la racha
            rachaContador += 1
            rachaRegistradaHoy = true //bloquea el boton
            diasCompletados = diasCompletados + hoyDiaStr //agrega el dia de hoy a la lista

            //guarda de forma permanente en el chip del telefono
            prefs.edit()
                .putInt("racha_contador", rachaContador)
                .putString("ultima_fecha_registro", hoy.toString())
                .putStringSet("dias_completados_set", diasCompletados)
                .apply()

            //Actualiza la lista visual de los dias para pintar los check
            generarDiasDeLaSemana()
        }
    }
    fun generarDiasDeLaSemana() {
        val hoy = LocalDate.now()
        val lunesDeEstaSemana = hoy.with(DayOfWeek.MONDAY)
        val lista = mutableListOf<DiaData>()

        for (i in 0..6) {
            val fechaDia = lunesDeEstaSemana.plusDays(i.toLong())
            val numeroDiaStr = fechaDia.dayOfMonth.toString()
            val nombreCorto = fechaDia.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale("es", "MX")
            ).replaceFirstChar { it.uppercase() }

            lista.add(
                DiaData(
                    nombre = nombreCorto,
                    numero = fechaDia.dayOfMonth.toString(),
                    esHoy = fechaDia.isEqual(hoy),
                    estaCompletado = diasCompletados.contains(numeroDiaStr)
                )
            )
        }
        diasSemanaActual = lista
    }
}
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
            titulo = "Hidratacion",
            imagenRes = R.drawable.img_hidratacion,
            rutaNavigation = "hidratacion",
            colorFondo = Color(0xFFB3E5FC)
        ),
        HabitoItem(
            titulo = "Sueño",
            imagenRes = R.drawable.img_sueno,
            rutaNavigation = "sueño",
            colorFondo = Color(0xFF1A237E)
        ),
        HabitoItem(
            titulo = "Actividad\nFisica",
            imagenRes = R.drawable.img_ejercicio,
            rutaNavigation = "actividad_fisica_mascota",
            colorFondo = Color(0xFFFFCDD2)
        ),
        HabitoItem(
            titulo = "Alimentacion",
            imagenRes = R.drawable.img_alimentacion,
            rutaNavigation = "alimentacion",
            colorFondo = Color(0xFFFFF9C4)
        ),
        HabitoItem(
            titulo = "mascotas",
            imagenRes = R.drawable.img_mascotas_v,
            rutaNavigation = "mascota_menu",
            colorFondo = Color(0xFFD7CCC8)
        ),
        HabitoItem(
            titulo = "Resumen",
            imagenRes = R.drawable.img_resumen,
            rutaNavigation = "perfil", //Agregar ruta. ejmp | resumen | Cuando se cree la pantalla
            colorFondo = Color(0xFFEE1BEE)
        )
    )

    //contenedor del menu despegable
    BaseCustomDrawer(navController = navController, drawerState = drawerState){
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
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = {
                            scoper.launch { drawerState.open() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
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
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Racha de hoy Lista",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "Activar Racha: ${viewModel.rachaContador} dias",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), //para que sean nomas dos columnas
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

@Composable
fun DiaItem(diaSemana: String, numeroDia: String, esHoy: Boolean, esCompletado: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(62.dp)
            .background(
                color = if (esHoy) Color (0xFFFFF200) else Color.Transparent,
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
        Text (
            text = numeroDia,
            color = if (esHoy) Color.Black else Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        if(esCompletado) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(if(esHoy) Color(0xFF2E7D32) else Color(0xFFFFF200), shape = CircleShape),
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
            //espacio para que no pierda altura
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun Tarjetahabito(habito: HabitoItem, onClick: () -> Unit) {
    val colorText = if (habito.colorFondo == Color(0xFF1A237E)) Color.White else Color.Black
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable{ onClick ()},
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
            Text (
                text = habito.titulo,
                color = Color.Black,
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
fun PreviewHabitos(){
    val nav = rememberNavController()
    HabitosScreen(navController = nav)
}