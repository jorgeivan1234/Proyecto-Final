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
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoHidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel(),
) {
    val scrollState = rememberScrollState()
    val metaLitros = viewModel.metaDiariaML / 1000f
    val cantidadVasos = (viewModel.metaDiariaML / 250)
    val basePeso = (viewModel.usuarioPeso * 35).toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tu plan de Hidratacion", fontWeight = FontWeight.Bold) },
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
        ){
            Text(
                text = "¡Meta Calculada!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFF7A22)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Necesitas tomar: ", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        text = String.format(Locale.US, "%.2f Litros", metaLitros),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF448AFF)
                    )
                    Text(text = "al dia", fontSize = 16.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Equivale a aproximadamente: ",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

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
                    text = " x $cantidadVasos vasos de 250ml",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Como llegamos a este numero?",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
            ){
                Column( modifier = Modifier.padding(16.dp)){
                    DesgloseItem(label="Base por peso (${viewModel.usuarioPeso}kg)", value = "${basePeso}ml")

                    if(viewModel.actividadNivel == "Intenso") {
                        DesgloseItem(label="Ajuste por actividad intensa", value = "+600ml", color = Color(0xFF2E7D32))
                    }
                    if(viewModel.entornoClima == "Calido") {
                        DesgloseItem(label="Ajuste por Clima calido", value = "+500ml", color = Color(0xFFD84315))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("hidratacion") {
                        popUpTo("hidratacion") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("¡Entendido, aceptar meta!", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DesgloseItem(label: String, value: String, color: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

