package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionMascotaViewModel
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.BlancoTarjeta90
import com.fic.dualhabit10.ui.theme.BlancoTarjeta80
import com.fic.dualhabit10.ui.theme.NaranjaTextoClima
import com.fic.dualhabit10.ui.theme.AzulTextoClima
import com.fic.dualhabit10.ui.theme.NaranjaFondoClima
import com.fic.dualhabit10.ui.theme.AzulFondoClima
import com.fic.dualhabit10.ui.theme.AzulFondoProgresoMascota
import com.fic.dualhabit10.ui.theme.AzulClimaFrio
import com.fic.dualhabit10.ui.theme.AzulBotonSumaOscuro
import com.fic.dualhabit10.ui.theme.RojoAlerta

// Pantalla principal encargada del monitoreo y registro del consumo diario de agua de la mascota
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HidratacionMascotaScreen(
    navController: NavController,
    viewModel: HidratacionMascotaViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val aguaConsumida = viewModel.aguaMascotaConsumidaML
    val metaDiaria = viewModel.metaMascotaDiariaML

    // Mapeo dinámico y seguro del porcentaje de progreso actual de hidratación
    val progreso = remember(aguaConsumida, metaDiaria) {
        if (metaDiaria > 0) {
            (aguaConsumida.toFloat() / metaDiaria.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .height(Dimens.topBarHeightExtra)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = Dimens.cornerRadiusExtraLarge,
                                bottomEnd = Dimens.cornerRadiusExtraLarge
                            )
                        ),
                    title = {
                        Box(
                            modifier = Modifier
                                .padding(top = Dimens.paddingExtraLarge)
                                .background(AmarilloFondo, shape = RoundedCornerShape(Dimens.cornerRadiusLarge))
                                .padding(horizontal = Dimens.paddingExtraLarge, vertical = Dimens.paddingSmall)
                        ) {
                            Text(
                                text = stringResource(R.string.title_hidratacion_mascota),
                                fontWeight = FontWeight.Bold,
                                color = TextoNegro,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.padding(start = Dimens.paddingSmall)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.desc_menu),
                                tint = TextoNegro,
                                modifier = Modifier.size(Dimens.iconSizeLarge)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = NaranjaCabecera
                    ),
                    windowInsets = WindowInsets.statusBars
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VerdeFondoHabitos)
                    .padding(innerPadding)
                    .padding(Dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingLarge)
            ) {
                InfoCardClima(viewModel)
                InfoCardMascota(viewModel)
                CardProgreso(aguaConsumida, metaDiaria, progreso)
                ControlesHidratacion(viewModel)
            }
        }
    }
}

// Tarjeta informativa que despliega las variables climatológicas del entorno para ajustar el consumo idóneo
@Composable
fun InfoCardClima(viewModel: HidratacionMascotaViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.cornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = BlancoTarjeta90),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingMediumSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.label_clima_api),
                    style = MaterialTheme.typography.labelSmall,
                    color = GrisTextoHint
                )
                Text(
                    text = stringResource(R.string.format_temperatura, viewModel.temperaturaActual.toString()),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = stringResource(if (viewModel.climaCaluroso) R.string.label_calor_detectado else R.string.label_clima_estable),
                color = if (viewModel.climaCaluroso) NaranjaTextoClima else AzulTextoClima,
                modifier = Modifier
                    .background(
                        color = if (viewModel.climaCaluroso) NaranjaFondoClima else AzulFondoClima,
                        shape = RoundedCornerShape(Dimens.cornerRadiusDefault)
                    )
                    .padding(Dimens.paddingSmallMedium),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

// Bloque de progreso visual del volumen ingerido frente al objetivo establecido del día
@Composable
fun CardProgreso(agua: Int, meta: Int, progreso: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = AzulFondoProgresoMascota),
        elevation = CardDefaults.cardElevation(Dimens.elevationSmall)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.label_progreso_hoy),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.format_progreso_ml, agua.toString(), meta.toString()),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold
            )

            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.cornerRadiusLarge)
                    .clip(RoundedCornerShape(Dimens.cornerRadiusSmall)),
                color = AzulClimaFrio,
                trackColor = Color.White
            )
        }
    }
}

// Tarjeta que detalla las características físicas base del animal registradas en la sesión
@Composable
fun InfoCardMascota(viewModel: HidratacionMascotaViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = BlancoTarjeta90),
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingMediumSmall)) {
            Text(
                text = stringResource(R.string.label_mascota_activa),
                style = MaterialTheme.typography.labelSmall,
                color = GrisTextoHint
            )
            Text(
                text = stringResource(R.string.format_mascota_peso, viewModel.tipoMascota, viewModel.pesoKG.toString()),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = NaranjaCabecera
            )
        }
    }
}

// Módulo interactivo de adición rápida de tomas de agua y controles adaptativos de esfuerzo físico
@Composable
fun ControlesHidratacion(viewModel: HidratacionMascotaViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BlancoTarjeta80),
        shape = RoundedCornerShape(Dimens.cornerRadiusDefault)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_hizo_ejercicio),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = Dimens.paddingSmall)
            )
            FilterChip(
                selected = viewModel.hizoEjercicio,
                onClick = {
                    viewModel.hizoEjercicio = !viewModel.hizoEjercicio
                    viewModel.calcularMetaInteligente()
                },
                label = { Text(stringResource(if (viewModel.hizoEjercicio) R.string.label_si else R.string.label_no)) },
                modifier = Modifier.padding(end = Dimens.paddingSmall)
            )
        }
    }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)) {
        Button(
            onClick = { viewModel.sumarAguaMascota(250) },
            modifier = Modifier
                .weight(1f)
                .height(Dimens.buttonHeight),
            colors = ButtonDefaults.buttonColors(containerColor = AzulClimaFrio),
            shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
        ) {
            Text(text = stringResource(R.string.btn_add_250ml), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = { viewModel.sumarAguaMascota(500) },
            modifier = Modifier
                .weight(1f)
                .height(Dimens.buttonHeight),
            colors = ButtonDefaults.buttonColors(containerColor = AzulBotonSumaOscuro),
            shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
        ) {
            Text(text = stringResource(R.string.btn_add_500ml), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

    OutlinedButton(
        onClick = { viewModel.reiniciarProgreso() },
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoAlerta)
    ) {
        Text(text = stringResource(R.string.btn_reiniciar_progreso))
    }
}

@Preview
@Composable
fun PreviewHidratacionMascotaScreen(){
    val nav = rememberNavController()
    HidratacionMascotaScreen(navController = nav)
}