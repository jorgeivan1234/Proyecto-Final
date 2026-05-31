package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionMascotaViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoHidratacionMascotaScreen(
    navController: NavController,
    viewModel: HidratacionMascotaViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val metaLitros = viewModel.metaMascotaDiariaML / 1000f
    val peso = viewModel.pesoKG.toFloatOrNull() ?: 10f
    val baseEspecieML = when (viewModel.tipoMascota){
        "Perro" -> peso * 60f
        "Gato" -> peso * 45f
        "Ave" -> peso * 8f
        "Roedor/Conejo" -> peso * 70f
        else -> peso * 50f
    }.toInt()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan de hidracion de mascotas", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF7A22))
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Meta de ${viewModel.tipoMascota}!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFF9A22)
            )
            Spacer(modifier = Modifier.height(16.dp))
            //tarjeta de litros o mililitros requerids
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ){
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Tu mascota necesita tomar: ", fontSize = 15.sp, color = Color.Gray)
                    Text(
                        text = "${viewModel.metaMascotaDiariaML} ml",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = String.format(Locale.US, "(~ %.2f Litros", metaLitros),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            //grafico descriptivo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_vaso),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = " Configuracion calculadora con exito",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Como se calculo esta meta?",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            //tarjeta con desglose de la formula inteligente
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F8FA)),
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    DesgloseItem(
                        label = "Base de ${viewModel.tipoMascota} (${viewModel.pesoKG} KG",
                        value = "${baseEspecieML} ml"
                    )

                    if (viewModel.tipoDieta.lowercase() == "humedo") {
                        DesgloseItem(
                            label = "Reduccion por dieta Humeda",
                            value = "x 0.7",
                            color = Color(0xFF2E7D32)
                        )
                    }
                    if (viewModel.climaCaluroso){
                        DesgloseItem(
                            label = "Aumento por clima caluroso (>= 30°C) ",
                            value = "x 1.4",
                            color = Color(0xFFD84315)
                        )
                    }
                    if (viewModel.hizoEjercicio) {
                        DesgloseItem(
                            label = "Incremento por ejercicio",
                            value = "x 1.2",
                            color = Color(0xFF0288D1)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            //boton de confirmacion para la pantalla de hidratacion
            Button(
                onClick = {
                    navController.navigate("hidratacion_mascota") {
                        popUpTo("hidratacion_mascota") {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("¡Aplicar meta al diario", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}