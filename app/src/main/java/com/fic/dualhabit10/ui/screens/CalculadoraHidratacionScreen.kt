package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraHidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString() )}
    var clima by remember { mutableStateOf(viewModel.entornoClima)}
    var actividad by remember  { mutableStateOf(viewModel.actividadNivel)}
    var expClima by remember { mutableStateOf(false)}
    var expActividad by remember { mutableStateOf(false)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calcular de Agua", fontWeight = FontWeight.Bold)},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras")
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
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_calculadora),
                contentDescription = "Calculadora",
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = "Ajusta tus parametros para recalcular tu requerimiento diario minimo de agua",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                label = { Text("Peso actual (Kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expActividad,
                onExpandedChange = { expActividad = !expActividad }
            ) {
                OutlinedTextField(
                    value = actividad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text ("Nivel de actividad fisica")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expActividad)},
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expActividad,
                    onDismissRequest = {expActividad = false}
                ) {
                    DropdownMenuItem(text = {Text("Sedentario (peso x 30)")}, onClick = { actividad = "Sedentario"; expActividad = false})
                    DropdownMenuItem(text = {Text("Moderado (peso x 35)")}, onClick = { actividad = "Moderado"; expActividad = false})
                    DropdownMenuItem(text = {Text("Intenso (peso x 40)")}, onClick = { actividad = "Intenso"; expActividad = false})
                }
            }
            ExposedDropdownMenuBox(
                expanded = expClima,
                onExpandedChange = {expClima = !expClima }
            ) {
                OutlinedTextField(
                    value = clima,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text ("Entorno Metereologico")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expClima) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expClima,
                    onDismissRequest = {expClima = false}
                ) {
                    DropdownMenuItem(text = { Text("Frio")}, onClick = { clima = "Frio"; expClima = false})
                    DropdownMenuItem(text = { Text("Calido / Muy Caluroso")}, onClick = { clima = "Calido"; expClima = false})
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.guardarPerfil(
                        peso.toFloatOrNull() ?: 70f,
                        viewModel.usuarioEdad,
                        viewModel.usuarioGenero,
                        actividad,
                        clima
                    )
                    navController.navigate("resultado_hidratacion")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22))
            ) {
                Text("Calcular y Actualizar Requerimiento", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}