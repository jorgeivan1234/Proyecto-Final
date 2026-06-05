package com.fic.dualhabit10.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HigieneMascotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigieneMascotaDetalleScreen(
    navController: NavController,
    viewModel: HigieneMascotaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val diasBano by viewModel.diasDesdeBano.collectAsState()
    val diasUnas by viewModel.diasDesdeUnas.collectAsState()
    
    //recargar datos para procesar contadores
    LaunchedEffect(Unit) {
        viewModel.cargarDatosDiarios()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fechas e Historial", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atras")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.azul_cielo)
                )
            )
        },
        containerColor = colorResource(id = R.color.azul_cielo)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cronograma de Cuidados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 20.dp)
            )

            //alerta de seccion: baño limite de 15 dias
            TarjetaAlertaPeriodica(
                titulo = "Ultimo baño",
                diasTranscurridos = diasBano,
                limiteDias = 15,
                onRegistrarHoy = { viewModel.registrarBano()}
            )

            Spacer(modifier = Modifier.height(20.dp))

            //alerta de seccion: uñas limite de 30 dias
            TarjetaAlertaPeriodica(
                titulo = "Ultimo corte de uñas",
                diasTranscurridos = diasUnas,
                limiteDias = 30,
                onRegistrarHoy = { viewModel.registrarCorteUnas()}
            )
        }
    }
}

@Composable
fun TarjetaAlertaPeriodica(
    titulo: String,
    diasTranscurridos: Long?,
    limiteDias: Int,
    onRegistrarHoy: () -> Unit
) {
    //determinar el color dele estado segun los dias pasados
    val colorEstado = when {
        diasTranscurridos == null -> Color.Gray
        diasTranscurridos >= limiteDias -> colorResource(id = R.color.rojo)
        diasTranscurridos >= (limiteDias - 4) -> Color(0xFFFFB300)
        else -> colorResource(id = R.color.verde)
    }
    val textoDias = when (diasTranscurridos) {
        null -> "Sin registros previos"
        0L -> "¡Se realizó hoy!"
        1L -> "Hace 1 día"
        else -> "Hace $diasTranscurridos días"
    }

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(colorEstado, RoundedCornerShape(50.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = textoDias,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorEstado
            )

            Text(
                text = "Intervalo recomendado: Cada $limiteDias días",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            //boton interactivo para registraee una accion
            Button(
                onClick = onRegistrarHoy,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.azul_fuerte)),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Hecho hoy", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
