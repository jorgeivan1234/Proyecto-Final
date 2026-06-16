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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.AzulFondoDesglose
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.GrisFondoProgreso
import com.fic.dualhabit10.ui.theme.AzulTarjetaProgreso
import com.fic.dualhabit10.ui.theme.VerdeCompletado

// Pantalla de historial de hidratación organizada por pestañas temporales
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

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
                    title = {
                        Text(
                            text = stringResource(R.string.title_historial_agua),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.desc_menu))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = NaranjaCabecera),
                    modifier = Modifier.clip(RoundedCornerShape(bottomStart = Dimens.cornerRadiusExtraLarge, bottomEnd = Dimens.cornerRadiusExtraLarge))
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VerdeFondoHabitos)
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(Dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingLarge)
            ) {

                // Selector de pestañas para alternar el rango temporal del historial
                Card(
                    shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
                    colors = CardDefaults.cardColors(containerColor = AzulFondoDesglose),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.paddingTiny),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        opcionesPestanas.forEachIndexed { indice, titulo ->
                            val seleccionado = pestanaSeleccionada == indice
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        color = if (seleccionado) NaranjaCabecera else Color.Transparent,
                                        shape = RoundedCornerShape(Dimens.cornerRadiusSmall)
                                    )
                                    .clickable { pestanaSeleccionada = indice }
                                    .padding(vertical = Dimens.paddingSmall),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = titulo,
                                    fontWeight = FontWeight.Bold,
                                    color = if (seleccionado) TextoNegro else GrisTextoHint,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

                // Contenedor dinámico que conmuta la vista según el filtro activo
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

// Componente de progreso diario que muestra el consumo actual frente a la meta fija
@Composable
fun VistaDiariaReal(viewModel: HidratacionViewModel) {
    val consumo = viewModel.aguaConsumidaML
    val meta = viewModel.metaDiariaML

    val porcentaje = if (meta > 0) (consumo.toFloat() / meta.toFloat()).coerceAtMost(1f) else 0f
    val textoPorcentaje = (porcentaje * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
        elevation = CardDefaults.cardElevation(Dimens.elevationSmall)
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingLarge), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.label_consumo_hoy), style = MaterialTheme.typography.titleMedium, color = GrisTextoHint)

            Text(
                text = "$consumo ml / $meta ml",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = AzulTarjetaProgreso
            )

            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            LinearProgressIndicator(
                progress = { porcentaje },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.progressBarHeight),
                color = AzulTarjetaProgreso,
                trackColor = GrisFondoProgreso,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
            Text(
                text = stringResource(R.string.label_meta_alcanzada, textoPorcentaje),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Gráfico de barras que representa el consumo de los últimos siete días
@Composable
fun VistaSemanalReal(viewModel: HidratacionViewModel) {
    val diasSemana = listOf(
        stringResource(R.string.dia_dom), stringResource(R.string.dia_lun), stringResource(R.string.dia_mar),
        stringResource(R.string.dia_mie), stringResource(R.string.dia_jue), stringResource(R.string.dia_vie),
        stringResource(R.string.dia_sab)
    )
    val hoy = LocalDate.now()

    // Cálculo del porcentaje de consumo para cada día de la semana
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
        shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
        elevation = CardDefaults.cardElevation(Dimens.elevationSmall)
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingDefault)) {
            Text(
                text = stringResource(R.string.label_desempeno_semanal),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = GrisOscuro
            )
            Spacer(modifier = Modifier.height(Dimens.paddingLarge))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.chartHeight),
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
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = GrisOscuro
                                )
                                Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight(porcentaje.coerceIn(0.01f, 1f))
                                    .background(
                                        color = if (porcentaje >= 1.0f) VerdeCompletado else AzulTarjetaProgreso,
                                        shape = RoundedCornerShape(topStart = Dimens.cornerRadiusTiny, topEnd = Dimens.cornerRadiusTiny)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimens.paddingSmall))
                        Text(text = nombreDia, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Panel estadístico que procesa el promedio, días cumplidos y efectividad del mes
@Composable
fun VistaMensualReal(viewModel: HidratacionViewModel) {
    val hoy = LocalDate.now()
    val composeLocale = androidx.compose.ui.text.intl.Locale.current
    val javaLocale = java.util.Locale.forLanguageTag(composeLocale.toLanguageTag())

    val nombreMes = hoy.month.getDisplayName(java.time.format.TextStyle.FULL, javaLocale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(javaLocale) else it.toString() }

    // Filtrado y cálculo de métricas para los registros del mes en curso
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
        shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
        elevation = CardDefaults.cardElevation(Dimens.elevationSmall)
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingLarge)){
            Text(
                text = stringResource(R.string.label_resumen_mes, nombreMes, hoy.year),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = GrisOscuro
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.label_promedio_diario), style = MaterialTheme.typography.bodyLarge)
                Text("${estadisticasMes.first} ml", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = AzulTarjetaProgreso)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.paddingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.label_dias_meta_cumplida), style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = stringResource(R.string.format_dias_de_dias, estadisticasMes.second.first, estadisticasMes.second.second),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = VerdeCompletado
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.paddingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.label_efectividad_mes), style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = estadisticasMes.third,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleLarge,
                    color = NaranjaCabecera
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistorialScreen() {
    val nav = rememberNavController()
    HistorialScreen(navController = nav)
}