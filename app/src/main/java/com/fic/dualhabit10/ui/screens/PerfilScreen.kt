package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel
import com.fic.dualhabit10.ui.viewmodels.PerfilMascotaViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.FondoOscuroTarjeta
import com.fic.dualhabit10.ui.theme.AzulBarraExp
import com.fic.dualhabit10.ui.theme.AmarilloNivel
import com.fic.dualhabit10.ui.theme.AzulClimaFrio
import com.fic.dualhabit10.ui.theme.AmarilloFondo


// Constantes
private object PerfilPrefs {
    const val FILE_NAME = "perfil_preferences"
    const val KEY_PROFILE_URI = "saved_profile_uri"
}

private object PerfilRoutes {
    const val MASCOTA = "perfil_mascota"
}

// Pantalla de Perfil Compartido
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel(),
    mascotaViewModel: PerfilMascotaViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val sharedPreferences = remember { context.getSharedPreferences(PerfilPrefs.FILE_NAME, Context.MODE_PRIVATE) }

    var peso by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var actividad by remember { mutableStateOf("") }

    val climaActual = remember(viewModel.entornoClima) { viewModel.entornoClima }

    LaunchedEffect(Unit) {
        peso = if (viewModel.usuarioPeso > 0f) viewModel.usuarioPeso.toString() else ""
        edad = if (viewModel.usuarioEdad > 0) viewModel.usuarioEdad.toString() else ""
        genero = viewModel.usuarioGenero.ifEmpty { "" }
        actividad = viewModel.actividadNivel.ifEmpty { "" }
    }

    var expGenero by remember { mutableStateOf(false) }
    var expActividad by remember { mutableStateOf(false) }

    var fotoUsuarioUri by remember {
        mutableStateOf<Uri?>(sharedPreferences.getString(PerfilPrefs.KEY_PROFILE_URI, null)?.let { Uri.parse(it) })
    }

    val fotoMascotaUri = remember(mascotaViewModel.imagenMascota) {
        if (mascotaViewModel.imagenMascota.isNotEmpty()) {
            try { Uri.parse(mascotaViewModel.imagenMascota) } catch(e: Exception) { null }
        } else null
    }

    val galeriaLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                fotoUsuarioUri = uri
                sharedPreferences.edit().putString(PerfilPrefs.KEY_PROFILE_URI, uri.toString()).apply()
            } catch (e: Exception) {
                fotoUsuarioUri = uri
                sharedPreferences.edit().putString(PerfilPrefs.KEY_PROFILE_URI, uri.toString()).apply()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_perfil_compartido),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextoNegro,
                        modifier = Modifier
                            .background(
                                color = AmarilloFondo,
                                shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                            )
                            .padding(
                                horizontal = Dimens.spacerMedium,
                                vertical = Dimens.paddingTiny
                            )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.guardarPerfil(peso.toFloatOrNull() ?: 70f, edad.toIntOrNull() ?: 25, genero, actividad)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = TextoNegro)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NaranjaCabecera),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = Dimens.paddingLarge, bottomEnd = Dimens.paddingLarge))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(Dimens.spacerMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault)
        ) {
            // Posicionamiento de perfiles lado a lado (Humano y Mascota)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Apartado Humano
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(Dimens.imageProfileSize)) {
                        AsyncImage(
                            model = fotoUsuarioUri ?: R.drawable.img_hidratacion,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(Dimens.borderThickness, TextoNegro, CircleShape)
                                .clickable { galeriaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                    Text(
                        text = stringResource(R.string.label_usuario),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.hint_cambiar_foto),
                        style = MaterialTheme.typography.labelSmall,
                        color = GrisTextoHint
                    )
                }

                // Apartado Mascota
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(Dimens.imageProfileSize)) {
                        AsyncImage(
                            model = fotoMascotaUri ?: R.drawable.img_mascotas_v,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(Dimens.borderThickness, TextoNegro, CircleShape)
                                .clickable { navController.navigate(PerfilRoutes.MASCOTA) },
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                    Text(
                        text = stringResource(R.string.label_mascota),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.hint_ver_perfil),
                        style = MaterialTheme.typography.labelSmall,
                        color = GrisTextoHint
                    )
                }
            }

            // Tarjeta de Nivel y Experiencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = FondoOscuroTarjeta)
            ) {
                Column(modifier = Modifier.padding(Dimens.paddingDefault)) {
                    Text(
                        text = stringResource(R.string.label_nivel_cuenta, viewModel.experienciaNivel),
                        color = AmarilloNivel,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    val progreso = remember(viewModel.experienciaNivel, viewModel.experienciaPuntos) {
                        if (viewModel.experienciaNivel > 0) viewModel.experienciaPuntos / (100f * viewModel.experienciaNivel) else 0f
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.paddingSmall),
                        color = AzulBarraExp
                    )
                    Text(
                        text = stringResource(R.string.label_exp_siguiente_logro),
                        color = TextoBlanco,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = Dimens.paddingTiny)
                    )
                }
            }

            // Campos de configuración
            Text(
                text = stringResource(R.string.title_config_biometrica),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = peso,
                onValueChange = { newValue ->
                    val filtered = newValue.replace(',', '.').filter { c -> c.isDigit() || c == '.' }
                    if (filtered.count { it == '.' } <= 1 && filtered.length <= 5) {
                        peso = filtered
                    }
                },
                label = { Text(stringResource(R.string.label_peso_actual)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = edad,
                onValueChange = { if (it.length <= 3) edad = it.filter { c -> c.isDigit() } },
                label = { Text(stringResource(R.string.label_edad)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
            )

            // Menú Género
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_genero)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expGenero) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NaranjaCabecera,
                        unfocusedBorderColor = GrisTextoHint
                    )
                )
                Box(modifier = Modifier.matchParentSize().clickable { expGenero = true })
                DropdownMenu(expanded = expGenero, onDismissRequest = { expGenero = false }, modifier = Modifier.fillMaxWidth(0.9f)) {
                    DropdownMenuItem(
                        text = { Text("Seleccionar") },
                        onClick = { genero = "Seleccionar"; expGenero = false }
                    )
                    stringArrayResource(R.array.generos_biologicos).forEach { opcion ->
                        DropdownMenuItem(text = { Text(opcion) }, onClick = { genero = opcion; expGenero = false })
                    }
                }
            }

            // Menú Actividad
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = actividad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_actividad)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expActividad) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NaranjaCabecera,
                        unfocusedBorderColor = GrisTextoHint
                    )
                )
                Box(modifier = Modifier.matchParentSize().clickable { expActividad = true })
                DropdownMenu(expanded = expActividad, onDismissRequest = { expActividad = false }, modifier = Modifier.fillMaxWidth(0.9f)) {
                    DropdownMenuItem(
                        text = { Text("Seleccionar") },
                        onClick = { actividad = "Seleccionar"; expActividad = false }
                    )
                    stringArrayResource(R.array.niveles_actividad).forEach { opcion ->
                        DropdownMenuItem(text = { Text(opcion) }, onClick = { actividad = opcion; expActividad = false })
                    }
                }
            }

            // Clima
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TextoBlanco.copy(alpha = 0.5f))
            ) {
                Row(modifier = Modifier.padding(Dimens.paddingDefault), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.label_entorno_clima),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(Dimens.paddingSmall))
                    Text(
                        text = climaActual,
                        color = if (climaActual == "Calido") NaranjaCabecera else AzulClimaFrio,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            Button(
                onClick = {
                    viewModel.guardarPerfil(peso.toFloatOrNull() ?: 70f, edad.toIntOrNull() ?: 25, genero, actividad)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.buttonHeight),
                colors = ButtonDefaults.buttonColors(containerColor = NaranjaCabecera)
            ) {
                Text(
                    text = stringResource(R.string.btn_guardar_recalcular),
                    color = TextoNegro,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}