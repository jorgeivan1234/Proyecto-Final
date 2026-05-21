package com.fic.dualhabit10.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HidratacionScreen(navController: NavController){
    //estado para controlar el agua consumida
    var aguaConsumida by remember { mutableIntStateOf(0) }
    val metaDiaria = 2000

    //animacion fluida para la barra de progreso basada en el porcentaje
    val porcentajeProgreso = (aguaConsumida.toFloat() / metaDiaria.toFloat()).coerceAtMost(1.0f)
    val progresoAnimado by animateFloatAsState(targetValue =  porcentajeProgreso, label = "progresoAgua")

    //barra superior
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9EFFEB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {navController.popBackStack() }){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "regresar",
                    tint = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            // contenedor principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ){
               Column(horizontalAlignment = Alignment.CenterHorizontally){
                   Text(
                       text = "$aguaConsumida / $metaDiaria ml",
                       color = Color(0xFFFF7A22),
                       fontSize = 36.sp,
                       fontWeight = FontWeight.Black
                   )
                   Spacer(modifier = Modifier.height(8.dp))
                   Text(
                       text = if (aguaConsumida >= metaDiaria) "¡Meta cumplida de hoy!" else "¡Mantente hidratado!",
                       color = Color.Black,
                       fontSize = 16.sp,
                       fontWeight = FontWeight.Medium
                   )
               }
                //barra de progreso interactivo llenando el agua
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color(0xFFD2D2D2), shape = RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    LinearProgressIndicator(
                        progress = { progresoAnimado},
                        modifier = Modifier
                            .fillMaxSize(),
                        color = Color(0xFF29B6F6),
                        trackColor = Color.Transparent
                    )
                }

                //botones rapidos
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    )  {
                        BotonMediaAgua(
                            cantidad = "+250ml",
                            subtitulo = "Un vaso corto",
                            modifier = Modifier.weight(1f),
                            onClick = { aguaConsumida += 250 }
                        )
                        BotonMediaAgua(
                            cantidad = "+500ml",
                            subtitulo = "Botella estandar",
                            modifier = Modifier.weight(1f),
                            onClick = { aguaConsumida += 500 }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BotonMediaAgua(
                            cantidad = "+750ml",
                            subtitulo = "Termo mediano",
                            modifier = Modifier.weight(1f),
                            onClick = { aguaConsumida += 750 }
                        )
                        BotonMediaAgua(
                            cantidad = "+Reiniciar",
                            subtitulo = "Limpiar dia",
                            containerColor = Color(0xFFE57373),
                            contentColor = Color.White,
                            modifier = Modifier.weight(1f),
                            onClick = { aguaConsumida = 0 }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BotonMediaAgua(
    cantidad: String,
    subtitulo: String,
    containerColor: Color = Color(0xFFFF7A22),
    contentColor: Color = Color.Black,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(containerColor,shape = RoundedCornerShape(20.dp))
            .clickable{onClick ()}
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = cantidad,
            color = containerColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitulo,
            color = if (containerColor == Color.White) Color.White.copy(alpha = 0.8f) else Color.DarkGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHidratacion(){
    HidratacionScreen(navController = rememberNavController())
}