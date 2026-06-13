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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisTextoHint

// Constantes de navegación
private object CalculadoraRoutes {
    const val RESULTADO = "resultado_hidratacion"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraHidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString()) }

    var actividad by remember { mutableStateOf(viewModel.actividadNivel) }
    var expActividad by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_calculadora_agua),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextoNegro,
                        modifier = Modifier
                            .background(
                                color = AmarilloFondo,
                                shape = RoundedCornerShape(percent = 50)
                            )
                            .padding(
                                horizontal = Dimens.paddingLarge,
                                vertical = Dimens.paddingTiny
                            )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_atras),
                            tint = TextoNegro
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NaranjaCabecera
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_calculadora),
                contentDescription = stringResource(R.string.desc_calculadora),
                modifier = Modifier.size(Dimens.imageCalculadoraSize)
            )

            Text(
                text = stringResource(R.string.instrucciones_calculadora),
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTextoHint,
                modifier = Modifier.padding(bottom = Dimens.paddingSmall)
            )

            // Campo de Peso Actual
            OutlinedTextField(
                value = peso,
                onValueChange = { input ->
                    // Convierte comas en puntos y permite solo números y puntos
                    var filtrado = input.replace(',', '.').filter { it.isDigit() || it == '.' }

                    // Ignora la acción de un segundo punto decimal
                    if (filtrado.count { it == '.' } > 1) {
                        filtrado = filtrado.substring(0, filtrado.lastIndexOf('.'))
                    }

                    // Fuerza un punto automáticamente al escribir el 4to carácter (si no hay punto ya)
                    if (filtrado.length == 4 && !filtrado.contains('.')) {
                        filtrado = "${filtrado.substring(0, 3)}.${filtrado.substring(3)}"
                    }

                    // Limita a un máximo de 4 dígitos numéricos
                    val cantDigitos = filtrado.count { it.isDigit() }
                    if (cantDigitos <= 4) {
                        peso = filtrado
                    }
                },
                label = { Text(stringResource(R.string.label_peso_actual)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
                singleLine = true,
                // Forzamos el teclado numérico
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(Dimens.cornerRadiusSmall)
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

            val estadoClimaTraducido = if (viewModel.entornoClima == "Calido") {
                stringResource(R.string.clima_calido)
            } else {
                stringResource(R.string.clima_frio)
            }

            val textoClimaInformativo = stringResource(
                R.string.format_clima,
                viewModel.temperaturaActual.toString(),
                estadoClimaTraducido
            )

            OutlinedTextField(
                value = textoClimaInformativo,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text(stringResource(R.string.label_entorno_meteo)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.cornerRadiusSmall)
            )

            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            Button(
                onClick = {
                    viewModel.guardarPerfil(
                        peso = peso.toFloatOrNull() ?: 70f,
                        edad = viewModel.usuarioEdad,
                        genero = viewModel.usuarioGenero,
                        actividad = actividad
                    )
                    navController.navigate(CalculadoraRoutes.RESULTADO)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.buttonHeight),
                colors = ButtonDefaults.buttonColors(containerColor = NaranjaCabecera),
                shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
            ) {
                Text(
                    text = stringResource(R.string.btn_calcular_actualizar),
                    color = TextoNegro,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}