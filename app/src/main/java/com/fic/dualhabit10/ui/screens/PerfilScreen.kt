package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel

// -------------------------------------------------------------------------------------------------
// Constantes de diseño
// -------------------------------------------------------------------------------------------------

// Se renombró a UserProfileColors para evitar conflictos
private object UserProfileColors {
    val Primary = Color(0xFFFF7A22)
    val Background = Color(0xFF9EFFEB)
    val TextMain = Color.Black
    val TextHint = Color.Gray
    val White = Color.White
    val CardBackground = Color.Black
    val ExpBar = Color(0xFF29B6F6)
    val LevelText = Color.Yellow
    val ClimaCalido = Color(0xFFFF7A22)
    val ClimaFrio = Color(0xFF1976D2)
}

// -------------------------------------------------------------------------------------------------
// Vista principal del perfil del usuario
// -------------------------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel(),
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Para que no se pierda la foto de perfil al cerrar la app
    val sharedPreferences = remember {
        context.getSharedPreferences("perfil_preferences", Context.MODE_PRIVATE)
    }

    // Estados para la entrada de parámetros biométricos
    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString()) }
    var edad by remember { mutableStateOf(viewModel.usuarioEdad.toString()) }
    val climaActual = viewModel.entornoClima
    var genero by remember { mutableStateOf(viewModel.usuarioGenero) }
    var actividad by remember { mutableStateOf(viewModel.actividadNivel) }

    var expGenero by remember { mutableStateOf(false) }
    var expActividad by remember { mutableStateOf(false) }

    // Recupera la foto del usuario localmente
    var fotoUsuarioUri by remember {
        mutableStateOf<Uri?>(
            sharedPreferences.getString("saved_profile_uri", null)?.let { Uri.parse(it) }
        )
    }

    var galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                fotoUsuarioUri = uri
                sharedPreferences.edit().putString("saved_profile_uri", uri.toString()).apply()
            } catch (e: Exception) {
                fotoUsuarioUri = uri
                sharedPreferences.edit().putString("saved_profile_uri", uri.toString()).apply()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_perfil_compartido), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.guardarPerfil(
                            peso.toFloatOrNull() ?: 70f,
                            edad.toIntOrNull() ?: 25,
                            genero,
                            actividad
                        )
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.desc_atras))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UserProfileColors.Primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UserProfileColors.Background)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (fotoUsuarioUri != null) {
                        AsyncImage(
                            model = fotoUsuarioUri,
                            contentDescription = stringResource(R.string.desc_foto_usuario),
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .border(3.dp, UserProfileColors.TextMain, CircleShape)
                                .clickable {
                                    galeriaLauncher.launch(
                                        androidx.activity.result.PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.img_hidratacion),
                            contentDescription = stringResource(R.string.desc_foto_humano),
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .border(3.dp, UserProfileColors.TextMain, CircleShape)
                                .clickable {
                                    galeriaLauncher.launch(
                                        androidx.activity.result.PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.label_usuario), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(stringResource(R.string.hint_cambiar_foto), fontSize = 10.sp, color = UserProfileColors.TextHint)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mascotas_v),
                        contentDescription = stringResource(R.string.desc_foto_mascota),
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(3.dp, UserProfileColors.TextMain, CircleShape)
                            .clickable {
                                navController.navigate("perfil_mascota")
                            },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.label_mascota), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(stringResource(R.string.hint_ver_perfil), fontSize = 10.sp, color = UserProfileColors.TextHint)
                }
            }

            // Sección de nivel de experiencia de la cuenta
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = UserProfileColors.CardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.label_nivel_cuenta, viewModel.experienciaNivel),
                        color = UserProfileColors.LevelText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    val progreso = if (viewModel.experienciaNivel > 0) {
                        viewModel.experienciaPuntos / (100f * viewModel.experienciaNivel)
                    } else {
                        0f
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        color = UserProfileColors.ExpBar
                    )
                    Text(
                        text = stringResource(R.string.label_exp_siguiente_logro),
                        color = UserProfileColors.White,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Text(
                text = stringResource(R.string.title_config_biometrica),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )

            // Sección de Peso y Edad con agregados de seguridad (ISO 25019 - Robustez):
            // - singleLine: Bloquea saltos de renglón dañinos para la interfaz.
            // - KeyboardType.Number / Next / Done: Configura el teclado óptimo para facilitar la usabilidad.
            // - filtered: Reemplaza comas por puntos, limpia caracteres inválidos y delimita físicamente
            //   los campos (máximo 5 caracteres para peso y 3 para edad).
            OutlinedTextField(
                value = peso,
                onValueChange = { newValue ->
                    val filtered = newValue.replace(',', '.').filter { it.isDigit() || it == '.' }
                    if (filtered.count { it == '.' } <= 1 && filtered.length <= 5) {
                        peso = filtered
                    }
                },
                label = { Text(stringResource(R.string.label_peso_actual)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = edad,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() }
                    if (filtered.length <= 3) {
                        edad = filtered
                    }
                },
                label = { Text(stringResource(R.string.label_edad)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            // Menú desplegable para la selección del Género Biológico desde string-array xml
            ExposedDropdownMenuBox(
                expanded = expGenero,
                onExpandedChange = { expGenero = !expGenero }
            ) {
                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_genero)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expGenero) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expGenero,
                    onDismissRequest = { expGenero = false }
                ) {
                    stringArrayResource(R.array.generos_biologicos).forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { genero = opcion; expGenero = false }
                        )
                    }
                }
            }

            // Menú desplegable para el nivel de actividad física desde string-array xml
            ExposedDropdownMenuBox(
                expanded = expActividad,
                onExpandedChange = { expActividad = !expActividad }
            ) {
                OutlinedTextField(
                    value = actividad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_actividad)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expActividad) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expActividad,
                    onDismissRequest = { expActividad = false }
                ) {
                    stringArrayResource(R.array.niveles_actividad).forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { actividad = opcion; expActividad = false }
                        )
                    }
                }
            }

            // Despliegue informativo del entorno meteorológico actual de la API
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = UserProfileColors.White.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_entorno_clima),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = climaActual,
                        color = if (climaActual == "Calido") UserProfileColors.ClimaCalido else UserProfileColors.ClimaFrio,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de guardado y recálculo de metas
            Button(
                onClick = {
                    viewModel.guardarPerfil(
                        peso.toFloatOrNull() ?: 70f,
                        edad.toIntOrNull() ?: 25,
                        genero,
                        actividad
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UserProfileColors.Primary)
            ) {
                Text(
                    text = stringResource(R.string.btn_guardar_recalcular),
                    color = UserProfileColors.TextMain,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}