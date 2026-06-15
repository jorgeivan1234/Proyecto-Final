package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.data.local.PaseoEntity
import com.fic.dualhabit10.ui.viewmodels.PaseosViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.GrisMedio
import com.fic.dualhabit10.ui.theme.BlancoFondo
import com.fic.dualhabit10.ui.theme.PastelVerde
import com.fic.dualhabit10.ui.theme.RojoEliminar

// Pantalla para el registro temporal, auditoría y control cronológico de los paseos de la mascota
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroPaseosScreen(
    navController: NavController,
    viewModel: PaseosViewModel = viewModel()
) {
    val listaPaseos by viewModel.listaPaseos.collectAsState()

    var minutosInput by remember { mutableStateOf("") }
    var notasInput by remember { mutableStateOf("") }
    var errorInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeFondoHabitos)
    ) {
        // Cabecera Estandarizada adaptada al sistema de diseño unificado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.topBarHeightExtra)
                .background(
                    color = NaranjaCabecera,
                    shape = RoundedCornerShape(
                        bottomStart = Dimens.cornerRadiusExtraLarge,
                        bottomEnd = Dimens.cornerRadiusExtraLarge
                    )
                )
                .padding(horizontal = Dimens.paddingMediumSmall)
                .padding(top = Dimens.paddingLarge),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { navController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.desc_volver),
                        tint = TextoNegro,
                        modifier = Modifier.size(Dimens.iconSizeExtraLarge)
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = AmarilloFondo,
                                shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                            )
                            .padding(
                                horizontal = Dimens.paddingLarge,
                                vertical = Dimens.paddingSmallMedium
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.title_registro_paseo),
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro,
                            fontSize = Dimens.textSizeBodyLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.size(Dimens.spacerHeader))
            }
        }

        // Contenido Principal optimizado para listas potencialmente extensas
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = Dimens.paddingDefault,
                    vertical = Dimens.paddingDefault
                ),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault)
        ){
            item {
                Card (
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BlancoFondo),
                    shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
                    elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationSmall)
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens.paddingDefault),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.title_anadir_paseo),
                            fontSize = Dimens.textSizeBody,
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(Dimens.paddingMediumSmall))

                        // Entrada numérica restrictiva para la duración fraccionaria o entera del paseo
                        OutlinedTextField(
                            value = minutosInput,
                            onValueChange = { newValue ->
                                var filtrado = newValue.filter { it.isDigit() || it == '.' }

                                val partes = filtrado.split('.')
                                if (partes.size > 2) {
                                    filtrado = partes[0] + "." + partes.drop(1).joinToString("")
                                }

                                val digitos = filtrado.filter { it.isDigit() }

                                if (digitos.length <= 4) {
                                    if (digitos.length == 4 && !filtrado.contains('.')) {
                                        filtrado = digitos.substring(0, 3) + "." + digitos.substring(3)
                                    }
                                    minutosInput = filtrado
                                    errorInput = false
                                }
                            },
                            label = { Text (stringResource(R.string.label_duracion_minutos))},
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = errorInput,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NaranjaCabecera,
                                focusedLabelColor = NaranjaCabecera,
                            )
                        )
                        Spacer(modifier = Modifier.height(Dimens.paddingTiny))

                        // Entrada de texto acotada para anotaciones contextuales sobre el paseo
                        OutlinedTextField(
                            value = notasInput,
                            onValueChange = { newValue ->
                                if (newValue.length <= 30) {
                                    notasInput = newValue
                                }
                            },
                            label = { Text(stringResource(R.string.label_notas_paseo))},
                            singleLine = true,
                            supportingText = {
                                Text(
                                    text = "${notasInput.length} / 30",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NaranjaCabecera,
                                focusedLabelColor = NaranjaCabecera,
                            )
                        )

                        Spacer(modifier = Modifier.height(Dimens.paddingDefault))

                        // Botón de confirmación que persiste la bitácora en la base de datos local
                        Button(
                            onClick = {
                                val mins = minutosInput.toFloatOrNull()?.toInt()
                                if (mins != null && mins > 0) {
                                    viewModel.agregarPaseo(
                                        minutos = mins,
                                        notas = notasInput.trim()
                                    )
                                    minutosInput = ""
                                    notasInput = ""
                                    errorInput = false
                                } else {
                                    errorInput = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NaranjaCabecera),
                            shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text(
                                text = stringResource(R.string.btn_guardar_paseo),
                                color = BlancoFondo,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.title_historial_paseos),
                    fontSize = Dimens.textSizeBody,
                    fontWeight = FontWeight.Bold,
                    color = GrisOscuro,
                    modifier = Modifier.padding(top = Dimens.paddingTiny)
                )
            }

            // Renderizado condicional basado en la presencia de datos históricos
            if (listaPaseos.isEmpty()) {
                item {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.paddingExtraLarge),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.msg_sin_paseos),
                            color = GrisMedio,
                            fontSize = Dimens.textSizeMedium
                        )
                    }
                }
            } else {
                items(listaPaseos, key = { it.id }) { paseo ->
                    ItemHistorialPaseo(
                        paseo = paseo,
                        onEliminar = { viewModel.eliminarPaseo(paseo) }
                    )
                }
            }
        }
    }
}

// Tarjeta informativa que detalla la duración, marcas de tiempo y notas de un evento específico
@Composable
fun ItemHistorialPaseo(paseo: PaseoEntity, onEliminar: () -> Unit) {
    val fechaFormateada = remember(paseo.id) {
        try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(paseo.fecha))
        } catch (e: Exception) {
            "--/--/----"
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PastelVerde),
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingDefault),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.format_duracion, paseo.minutos.toString()),
                    fontSize = Dimens.textSizeBody,
                    fontWeight = FontWeight.Bold,
                    color = TextoNegro
                )
                Spacer(modifier = Modifier.height(Dimens.paddingMicro))
                Text(
                    text = stringResource(R.string.format_fecha, fechaFormateada),
                    fontSize = Dimens.textSizeSmall,
                    color = GrisOscuro
                )
                if (paseo.notas.isNotBlank()) {
                    Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                    Text(
                        text = stringResource(R.string.format_notas, paseo.notas),
                        fontSize = Dimens.textSizeSmall,
                        color = TextoNegro
                    )
                }
            }
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.desc_eliminar_registro),
                    tint = RojoEliminar
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistroPaseosScreen() {
    val nav = rememberNavController()
    RegistroPaseosScreen(navController = nav)
}