package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.AzulTarjetaProgreso
import com.fic.dualhabit10.ui.theme.AzulFondoDesglose
import com.fic.dualhabit10.ui.theme.VerdeCompletado
import com.fic.dualhabit10.ui.theme.NaranjaCalor

// Constantes de navegación
private object ResultadoRoutes {
    const val HABITOS_HIDRATACION = "hidratacion"
}

// Vista analítica que procesa, desglosa y presenta la meta de consumo de agua personalizada para el usuario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoHidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel(),
) {
    val scrollState = rememberScrollState()

    val metaLitros = viewModel.metaDiariaML / 1000f
    val cantidadVasos = if (viewModel.metaDiariaML > 0) viewModel.metaDiariaML / 250 else 0
    val basePeso = (viewModel.usuarioPeso * 35).toInt()

    Scaffold(
        topBar = {
            // Cabecera unificada y estilizada con soporte para barras de estado del sistema
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = NaranjaCabecera,
                        shape = RoundedCornerShape(
                            bottomStart = Dimens.cornerRadiusLarge,
                            bottomEnd = Dimens.cornerRadiusLarge
                        )
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
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.title_plan_hidratacion),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextoNegro,
                        modifier = Modifier
                            .background(
                                color = AmarilloFondo,
                                shape = RoundedCornerShape(percent = 50)
                            )
                            .padding(
                                horizontal = Dimens.paddingLarge,
                                vertical = Dimens.paddingTiny
                            )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(R.string.label_meta_calculada),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = NaranjaCabecera
            )
            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            // Tarjeta de visualización principal que destaca la meta diaria en litros
            Card(
                shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(Dimens.elevationMedium)
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.paddingXXLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.label_necesitas_tomar),
                        style = MaterialTheme.typography.titleMedium,
                        color = GrisTextoHint
                    )

                    Text(
                        text = stringResource(R.string.format_litros, metaLitros),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulTarjetaProgreso
                    )

                    Text(
                        text = stringResource(R.string.label_al_dia),
                        style = MaterialTheme.typography.titleMedium,
                        color = GrisTextoHint
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimens.paddingLarge))

            Text(
                text = stringResource(R.string.label_equivale_a),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))

            // Fila ilustrativa para la equivalencia volumétrica traducida a vasos estándar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_vaso),
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconSizeExtraLarge)
                )
                Spacer(modifier = Modifier.width(Dimens.paddingSmall))
                Text(
                    text = stringResource(R.string.format_vasos, cantidadVasos),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(Dimens.paddingLarge))

            Text(
                text = stringResource(R.string.label_como_llegamos),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Contenedor analítico que detalla las adiciones por factores biológicos y ambientales
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AzulFondoDesglose)
            ){
                Column( modifier = Modifier.padding(Dimens.paddingDefault)){
                    // Consumo base estimado de acuerdo al peso corporal actual
                    DesgloseItem(
                        label = stringResource(R.string.format_desglose_base, viewModel.usuarioPeso.toString()),
                        value = stringResource(R.string.format_ml, basePeso.toString())
                    )

                    // Incremento condicional basado en la intensidad del desgaste por actividad física
                    if(viewModel.actividadNivel == "Alto" || viewModel.actividadNivel == "Intenso") {
                        DesgloseItem(
                            label = stringResource(R.string.format_desglose_actividad, viewModel.actividadNivel.lowercase()),
                            value = stringResource(R.string.ajuste_600ml),
                            color = VerdeCompletado
                        )
                    }

                    // Incremento condicional debido al estrés térmico por factores climatológicos extremos
                    if(viewModel.entornoClima == "Calido" || viewModel.entornoClima == "Extremo") {
                        DesgloseItem(
                            label = stringResource(R.string.format_desglose_clima, viewModel.entornoClima.lowercase()),
                            value = stringResource(R.string.ajuste_500ml),
                            color = NaranjaCalor
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dimens.paddingXXLarge))

            // Botón de confirmación que consolida el registro y purga el historial de navegación hacia atrás
            Button(
                onClick = {
                    navController.navigate(ResultadoRoutes.HABITOS_HIDRATACION) {
                        popUpTo(ResultadoRoutes.HABITOS_HIDRATACION) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.buttonHeight),
                colors = ButtonDefaults.buttonColors(containerColor = NaranjaCabecera),
                shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
            ) {
                Text(
                    text = stringResource(R.string.btn_aceptar_meta),
                    color = TextoNegro,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Componente utilitario reutilizable para dar formato tabular a los elementos del desglose metabólico
@Composable
fun DesgloseItem(label: String, value: String, color: Color = TextoNegro) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.paddingTiny),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResultadoHidratacionScreen() {
    val nav = rememberNavController()
    ResultadoHidratacionScreen(navController = nav)
}