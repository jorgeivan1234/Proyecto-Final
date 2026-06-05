package com.fic.dualhabit10.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    // Controlamos la pestaña activa utilizando mutableIntStateOf para optimizar el consumo de memoria
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    // Lista de pestañas localizada desde el archivo de recursos independientes
    val opcionesPestanas = listOf(
        stringResource(R.string.tab_dia),
        stringResource(R.string.tab_semana),
        stringResource(R.string.tab_mes)
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.title_historial_agua), fontWeight = FontWeight.Bold)},
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.desc_menu))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF7A22)),
                    modifier = Modifier.clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF9EFFEB))
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        opcionesPestanas.forEachIndexed { indice, titulo ->
                            val seleccionado = pestanaSeleccionada == indice
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        color = if (seleccionado) Color(0xFFFF7A22) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { pestanaSeleccionada = indice }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = titulo,
                                    fontWeight = FontWeight.Bold,
                                    color = if (seleccionado) Color.Black else Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    when (pestanaSeleccionada){
                        0 -> VistaDiariaReal(viewModel)
                        1 -> VistaSemanalReal(viewModel)
                        2 -> VistaMensualReal(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun VistaDiariaReal(viewModel: HidratacionViewModel) {
    val consumo = viewModel.aguaConsumidaML
    val meta = viewModel.metaDiariaML

    val porcentaje = if (meta > 0) (consumo.toFloat() / meta.toFloat()).coerceAtMost(1f) else 0f
    val textoPorcentaje = (porcentaje * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.label_consumo_hoy), fontSize = 16.sp, color = Color.Gray)
            Text("$consumo ml / $meta ml", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color(0xFF448AFF))
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { porcentaje },
                modifier = Modifier.fillMaxWidth().height(14.dp),
                color = Color(0xFF448AFF),
                trackColor = Color(0xFFE0E0E0),
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.label_meta_alcanzada, textoPorcentaje), fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun VistaSemanalReal(viewModel: HidratacionViewModel) {
    val diasSemana = listOf(
        stringResource(R.string.dia_dom), stringResource(R.string.dia_lun), stringResource(R.string.dia_mar),
        stringResource(R.string.dia_mie), stringResource(R.string.dia_jue), stringResource(R.string.dia_vie),
        stringResource(R.string.dia_sab)
    )
    val hoy = LocalDate.now()

    val consumoSemanaPorcentaje = remember(viewModel.historialConsumoMap, viewModel.metaDiariaML) {
        List(7) { i ->
            val fechaDia = hoy.minusDays((6 - i).toLong())
            val consumoDia = viewModel.historialConsumoMap[fechaDia.toString()] ?: 0
            val nombreDia = diasSemana[fechaDia.dayOfWeek.value % 7]
            val porcentaje = if (viewModel.metaDiariaML > 0) consumoDia.toFloat() / viewModel.metaDiariaML.toFloat() else 0f
            Pair(nombreDia, porcentaje)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.label_desempeno_semanal), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                consumoSemanaPorcentaje.forEach { (nombreDia, porcentaje) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom,
                        ){
                            val porcentajeTexto = (porcentaje * 100).toInt()
                            if (porcentajeTexto > 0){
                                Text(
                                    text ="$porcentajeTexto%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        Box(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight(porcentaje.coerceIn(0.01f, 1f))
                                    .background(
                                        color = if (porcentaje >= 1.0f) Color(0xFF2E7D32) else Color(0xFF448AFF),
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = nombreDia, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun VistaMensualReal(viewModel: HidratacionViewModel) {
    val hoy = LocalDate.now()

    // Obtenemos el locale actual mediante el sistema observable de Compose
    val composeLocale = androidx.compose.ui.text.intl.Locale.current
    val javaLocale = java.util.Locale.forLanguageTag(composeLocale.toLanguageTag())

    val nombreMes = hoy.month.getDisplayName(java.time.format.TextStyle.FULL, javaLocale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(javaLocale) else it.toString() }

    // Agrupamos cálculos internos
    val estadisticasMes = remember(viewModel.historialConsumoMap, viewModel.metaDiariaML){
        val prefijoMes = hoy.toString().substring(0, 7)
        val registrosDelMes = viewModel.historialConsumoMap.filter { it.key.startsWith(prefijoMes) }

        val totalDiasRegistrados = registrosDelMes.size
        val sumaConsumo = registrosDelMes.values.sum()
        val promedio = if (totalDiasRegistrados > 0) sumaConsumo / totalDiasRegistrados else 0
        val diasCumplidos = registrosDelMes.values.count { it >= viewModel.metaDiariaML }
        val efectividad = if (totalDiasRegistrados > 0) (diasCumplidos.toFloat() / totalDiasRegistrados.toFloat() * 100).toInt() else 0

        Triple(promedio, Pair(diasCumplidos, totalDiasRegistrados), "$efectividad%")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)){
            Text(stringResource(R.string.label_resumen_mes, nombreMes, hoy.year), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.label_promedio_diario))
                Text("${estadisticasMes.first} ml", fontWeight = FontWeight.Bold, color = Color(0xFF448AFF))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.label_dias_meta_cumplida))
                Text(stringResource(R.string.format_dias_de_dias, estadisticasMes.second.first, estadisticasMes.second.second), fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.label_efectividad_mes))
                Text(estadisticasMes.third, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFFFF7A22))
            }
        }
    }
}