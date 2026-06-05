package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HigieneMascotaViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigieneMascotaScreen(
    navController: NavController,
    viewModel: HigieneMascotaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val estadoHigiene by viewModel.estadoHigiene.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.cargarDatosDiarios(LocalDate.now().toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Higiene de Mascota", fontWeight = FontWeight.Bold) },
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
                text = "Tareas Diarias",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
            )
            // tarjetas de habitos diarios
            ItemHigieneDiaria(
                titulo = "Higiene Dental",
                descripcion = "Cepillado o snack para el sarro",
                icono = Icons.Default.Pets,
                checkColor = colorResource(id = R.color.azul_fuerte),
                isChecked = estadoHigiene.higieneDental,
                onCheckedChange = { viewModel.actualizarHabitoDiario("dental", it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ItemHigieneDiaria(
                titulo = "Cepillado de pelo",
                descripcion = "Evita nudos y reduce la caida",
                icono = Icons.Default.CrueltyFree,
                checkColor = colorResource(id = R.color.verde),
                isChecked = estadoHigiene.cepilladoPelo,
                onCheckedChange = { viewModel.actualizarHabitoDiario("cepillado", it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ItemHigieneDiaria(
                titulo = "Limpieza del entorno",
                descripcion = "Lavar platos o limpiar espacio",
                icono = Icons.Default.CleanHands,
                checkColor = Color(0xFFE91E63),
                isChecked = estadoHigiene.limpiezaEntorno,
                onCheckedChange = { viewModel.actualizarHabitoDiario("entorno", it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // tarjeta de navegacion al control a largo plazo
            Card(
                onClick = { navController.navigate("higiene_mascota_detalle") },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.azul)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPasteSearch,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Control de Baño y Uñas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Revisa alertas de vencimiento",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemHigieneDiaria(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    checkColor: Color,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(checkColor.copy(alpha = 0.15f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = checkColor
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = descripcion,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = checkColor
                )
            )
        }
    }
}
