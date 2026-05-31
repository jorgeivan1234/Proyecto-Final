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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HidratacionMascotaScreen(
    navController: NavController,
    viewModel: HidratacionMascotaViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val aguaConsumida = viewModel.aguaMascotaConsumidaML
    val metaDiaria = viewModel.metaMascotaDiariaML

    // porcentaje tomando para la barra de progreso
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
                        .height(110.dp)
                        .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                    title = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 25.dp, vertical = 8.dp)
                        ) {
                            Text(
                                "Hidratacion Mascota",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 18.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.Black,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFFFF7A22)
                    ),
                    windowInsets = WindowInsets.statusBars
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                //card de clima
                InfoCardClima(viewModel)
                //card de mascota
                InfoCardMascota(viewModel)
                //card de progreso
                CardProgreso(aguaConsumida, metaDiaria, progreso)
                //botones de suma
                ControlesHidratacion(viewModel)
            }
        }
    }
}

@Composable
fun InfoCardClima(viewModel: HidratacionMascotaViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Clima Culiacan (API)", fontSize = 11.sp, color = Color.Gray)
                Text("${viewModel.temperaturaActual}°C", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = if (viewModel.climaCaluroso) "Calor detectado" else "Clima estable",
                color = if (viewModel.climaCaluroso) Color(0xFFE65100) else Color(0xFF0D47A1),
                modifier = Modifier
                    .background(if (viewModel.climaCaluroso) Color(0xFFFFE0B2) else Color(0xFFE3F2FD),
                        RoundedCornerShape(8.dp))
                    .padding(6.dp),
                fontWeight = FontWeight.Bold, fontSize = 12.sp
            )
        }
    }
}

@Composable
fun CardProgreso(agua: Int, meta: Int, progreso: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Progreso de Hoy", fontWeight = FontWeight.Bold)
            Text("$agua / $meta ml", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF1976D2),
                trackColor = Color.White
            )
        }
    }
}

@Composable
fun InfoCardMascota(viewModel: HidratacionMascotaViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Mascota activa en Room", fontSize = 11.sp, color = Color.Gray)
            Text(
                "${viewModel.tipoMascota} con ${viewModel.pesoKG} KG ",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF7A22)
            )
        }
    }
}

@Composable
fun ControlesHidratacion(viewModel: HidratacionMascotaViewModel) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿Hizo ejercicio hoy?", fontSize = 13.sp)
            FilterChip(
                selected = viewModel.hizoEjercicio,
                onClick = {
                    viewModel.hizoEjercicio = !viewModel.hizoEjercicio
                    viewModel.calcularMetaInteligente()
                          },
                label = { Text(if (viewModel.hizoEjercicio) "Sí" else "No") },
            )
        }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = {viewModel.sumarAguaMascota(250)}, Modifier.weight(1f).height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)), shape = RoundedCornerShape(16.dp)) {
            Text("+250 ml", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Button(onClick = {viewModel.sumarAguaMascota(500)}, Modifier.weight(1f).height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)), shape = RoundedCornerShape(16.dp)) {
            Text("+500 ml", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
    OutlinedButton(onClick = {viewModel.reiniciarProgreso() }, Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Red)) {
        Text("Reiniciar Progreso")
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