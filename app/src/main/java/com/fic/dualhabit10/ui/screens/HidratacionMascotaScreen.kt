package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.ui.viewmodels.HidratacionMascotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HidratacionMascotaScreen(
    navController: NavController,
    viewModel: HidratacionMascotaViewModel = viewModel()
) {
    val aguaConsumida = viewModel.aguaMascotaConsumidaML
    val metaDiaria = viewModel.metaMascotaDiariaML

    // porcentaje tomando para la barra de progreso
    val progreso = remember(aguaConsumida, metaDiaria){
        if (metaDiaria > 0) {
            (aguaConsumida.toFloat() / metaDiaria.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        Text("hidratacion Mascota", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF7A22))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    Text(
                        text = "Progreso de Hoy",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "$aguaConsumida / $metaDiaria ml",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp)),
                        color = Color(0xFF1976D2),
                        trackColor = Color.White
                    )

                    Text(
                        text = "Equivale al ${ (progreso * 100).toInt()}% de su mate diaria recomendada.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                }
            }

            Text(
                text = "¿Cuanta agua consumio tu maascota?",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {viewModel.sumarAguaMascota(250)},
                    modifier = Modifier.weight(1f).height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("+250 ml", fontSize = 16.sp, fontWeight = boldOrNormal(true), color = Color.White)
                }
                Button(
                    onClick = { viewModel.sumarAguaMascota(500) },
                    modifier = Modifier.weight(1f).height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("+500 ml", fontSize = 16.sp, color = Color.White)
                }
            }

            OutlinedButton(
                onClick = { viewModel.reiniciarProgreso()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Reiniciar Registro Diario", fontWeight = FontWeight.Medium)
            }
        }
    }
}


//extension rapida para la legibilidad del texto en el boton
private fun boldOrNormal(bold: Boolean) = if (bold) FontWeight.Bold else FontWeight.Normal

@Preview
@Composable
fun PreviewHidratacionMascotaScreen(){
    val nav = rememberNavController()
    HidratacionMascotaScreen(navController = nav)
}