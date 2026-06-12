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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraHidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString()) }

    // Mantenemos el estado interno de la lógica original para no romper el ViewModel
    var actividad by remember { mutableStateOf(viewModel.actividadNivel) }
    var expActividad by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Usamos CenterAlignedTopAppBar para centrar el contenido automáticamente
            androidx.compose.material3.CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_calculadora_agua),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFE033), // Amarillo vibrante
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(percent = 50)
                            )
                            // Espaciado interno de la píldora para que el texto respire
                            .padding(horizontal = 24.dp, vertical = 6.dp,)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.desc_atras))
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
                contentDescription = stringResource(R.string.desc_calculadora),
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(R.string.instrucciones_calculadora),
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                label = { Text(stringResource(R.string.label_peso_actual)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            // Mapeo del estado interno al String traducido para mostrarlo al usuario
            val actividadMostrada = when (actividad) {
                "Sedentario" -> stringResource(R.string.actividad_sedentario)
                "Moderado" -> stringResource(R.string.actividad_moderado)
                "Intenso" -> stringResource(R.string.actividad_intenso)
                else -> actividad
            }

            ExposedDropdownMenuBox(
                expanded = expActividad,
                onExpandedChange = { expActividad = !expActividad }
            ) {
                OutlinedTextField(
                    value = actividadMostrada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_nivel_actividad)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expActividad) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expActividad,
                    onDismissRequest = { expActividad = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.actividad_sedentario)) },
                        onClick = { actividad = "Sedentario"; expActividad = false }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.actividad_moderado)) },
                        onClick = { actividad = "Moderado"; expActividad = false }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.actividad_intenso)) },
                        onClick = { actividad = "Intenso"; expActividad = false }
                    )
                }
            }

            // Traducción dinámica del clima
            val estadoClimaTraducido = if (viewModel.entornoClima == "Calido") {
                stringResource(R.string.clima_calido)
            } else {
                stringResource(R.string.clima_frio)
            }

            // Utilizamos la herramienta String para introducir variables
            val textoClimaInformativo = stringResource(R.string.format_clima, viewModel.temperaturaActual.toString(), estadoClimaTraducido)

            OutlinedTextField(
                value = textoClimaInformativo,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text(stringResource(R.string.label_entorno_meteo)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.guardarPerfil(
                        peso = peso.toFloatOrNull() ?: 70f,
                        edad = viewModel.usuarioEdad,
                        genero = viewModel.usuarioGenero,
                        actividad = actividad
                    )
                    navController.navigate("resultado_hidratacion")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22))
            ) {
                Text(
                    text = stringResource(R.string.btn_calcular_actualizar),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}