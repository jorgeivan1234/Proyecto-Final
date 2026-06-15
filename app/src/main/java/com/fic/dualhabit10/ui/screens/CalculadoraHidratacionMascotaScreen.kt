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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

// Pantalla encargada de recopilar los parámetros y calcular las necesidades de hidratación de la mascota
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraHidratacionMacotaScreen(
    navController: NavController,
    viewModel: HidratacionMascotaViewModel = viewModel()
) {
    // Inicialización de los estados necesarios para manejar la entrada de datos del usuario
    val scrollState = rememberScrollState()
    var dieta by remember { mutableStateOf(viewModel.tipoDieta) }
    var hizoEjercicio by remember { mutableStateOf(viewModel.hizoEjercicio) }
    var climaCaluroso by remember { mutableStateOf(viewModel.climaCaluroso) }
    var expDieta by remember { mutableStateOf(false) }

    // Estructura principal de la vista con barra superior de navegación
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cálculo de agua para mascota", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF7A22))
            )
        }
    ) { innerPadding ->

        // Contenedor principal con capacidad de desplazamiento vertical para dispositivos de pantallas pequeñas
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_calculadora),
                contentDescription = "Calculadora de mascota",
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "Ajusta los parámetros diarios de tu mascota (${viewModel.tipoMascota}) para calcular su requerimiento hídrico:",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Campo de texto de solo lectura para mostrar el peso registrado en el perfil
            OutlinedTextField(
                value = "${viewModel.pesoKG} KG",
                onValueChange = {},
                readOnly = true,
                label = { Text("Peso actual de la mascota (Definido en perfil)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(disabledBorderColor = Color.Gray)
            )

            // Menú desplegable para seleccionar el tipo de dieta alimenticia
            ExposedDropdownMenuBox(
                expanded = expDieta,
                onExpandedChange = { expDieta = !expDieta }
            ) {
                OutlinedTextField(
                    value = if (dieta.lowercase() == "humedo") "Húmedo (Aporta agua al cuerpo)" else "Seco (Croquetas tradicionales)",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de dieta alimenticia") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expDieta) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expDieta,
                    onDismissRequest = { expDieta = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Seco (Multiplica base x 1.0)") },
                        onClick = { dieta = "Seco"; expDieta = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Húmedo (Reduce base x 0.7)") },
                        onClick = { dieta = "Humedo"; expDieta = false }
                    )
                }
            }

            // Interruptor para indicar si la mascota realizó actividad física durante el día
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("¿Realizó actividad física hoy?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        "Incrementa un 20% el consumo necesario por desgaste físico.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Switch(
                    checked = hizoEjercicio,
                    onCheckedChange = { hizoEjercicio = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFF7A22))
                )
            }

            // Interruptor para indicar si el entorno presenta altas temperaturas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        "¿El entorno presenta altas temperaturas?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "Aumenta un 40% el requerimiento (Temp >= 30°C).",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Switch(
                    checked = climaCaluroso,
                    onCheckedChange = { climaCaluroso = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFF7A22))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de acción para procesar los datos y navegar a la pantalla de resultados
            Button(
                onClick = {
                    viewModel.guardarPerfilMascota(
                        dieta = dieta,
                        ejercicio = hizoEjercicio,
                        climaManualCaluroso = climaCaluroso
                    )
                    navController.navigate("resultado_hidratacion_mascota")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22))
            ) {
                Text(
                    "Calcular requerimiento hídrico",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}