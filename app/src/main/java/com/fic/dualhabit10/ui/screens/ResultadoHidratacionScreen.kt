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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoHidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel(),
) {
    val scrollState = rememberScrollState()

    // Pasamos los ml a litros (ej. 2500 -> 2.5) para que la tarjeta principal no asuste al usuario con números enormes
    val metaLitros = viewModel.metaDiariaML / 1000f

    // Calculamos a cuántos vasos estándar equivale la meta. Usamos división entera por simplicidad
    val cantidadVasos = if (viewModel.metaDiariaML > 0) viewModel.metaDiariaML / 250 else 0
    val basePeso = (viewModel.usuarioPeso * 35).toInt()

    Scaffold(
        topBar = {
            // Implementamos la cabecera personalizada (estilo píldora) manteniendo consistencia con las otras pantallas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFFF7A22),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
                    // Aquí controlamos la distancia exacta desde arriba (28.dp)
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 28.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Píldora amarilla centrada
                    Text(
                        text = stringResource(R.string.title_plan_hidratacion),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp, // Ajustamos a 24.sp para que no rompa en pantallas pequeñas, pero sigue destacando
                        color = Color.Black,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFF200),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(R.string.label_meta_calculada),
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFF7A22)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta principal que destaca los litros totales en azul
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.label_necesitas_tomar), fontSize = 16.sp, color = Color.Gray)

                    // Ya no usamos String.format de Java, le delegamos el formato de 2 decimales directo al stringResource nativo de Compose
                    Text(
                        text = stringResource(R.string.format_litros, metaLitros),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF448AFF)
                    )

                    Text(text = stringResource(R.string.label_al_dia), fontSize = 16.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Sección visual para que el usuario dimensione la meta en vasos (más digerible que hablar de litros)
            Text(
                text = stringResource(R.string.label_equivale_a),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_vaso),
                    contentDescription = null, // Dejamos nulo porque el texto de al lado ya explica qué es
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = stringResource(R.string.format_vasos, cantidadVasos),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Detalle matemático: Mostramos de forma transparente cómo el ViewModel calculó esa meta
            Text(
                text = stringResource(R.string.label_como_llegamos),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
            ){
                Column( modifier = Modifier.padding(16.dp)){
                    DesgloseItem(
                        label = stringResource(R.string.format_desglose_base, viewModel.usuarioPeso.toString()),
                        value = stringResource(R.string.format_ml, basePeso.toString())
                    )

                    // Verificamos si hubo un recargo por actividad física
                    if(viewModel.actividadNivel == "Alto" || viewModel.actividadNivel == "Intenso") {
                        DesgloseItem(
                            label = stringResource(R.string.format_desglose_actividad, viewModel.actividadNivel.lowercase()),
                            value = stringResource(R.string.ajuste_600ml),
                            color = Color(0xFF2E7D32) // Verde para resaltar salud
                        )
                    }

                    // Verificamos si hubo un recargo por estar asándose de calor
                    if(viewModel.entornoClima == "Calido" || viewModel.entornoClima == "Extremo") {
                        DesgloseItem(
                            label = stringResource(R.string.format_desglose_clima, viewModel.entornoClima.lowercase()),
                            value = stringResource(R.string.ajuste_500ml),
                            color = Color(0xFFD84315) // Rojo/Naranja para representar calor
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Botón de confirmación que resetea el stack de navegación para no poder "volver atrás" a la pantalla de resultados
            Button(
                onClick = {
                    navController.navigate("hidratacion") {
                        popUpTo("hidratacion") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(stringResource(R.string.btn_aceptar_meta), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// Componente reutilizable para pintar cada renglón del desglose sin tener que repetir la fila entera 3 veces
@Composable
fun DesgloseItem(label: String, value: String, color: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
