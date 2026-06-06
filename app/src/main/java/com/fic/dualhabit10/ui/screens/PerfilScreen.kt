package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import com.fic.dualhabit10.ui.viewmodels.PerfilMascotaViewModel

// -------------------------------------------------------------------------------------------------
// Paleta de colores de la pantalla
// -------------------------------------------------------------------------------------------------
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
// Pantalla de Perfil Compartido (Humano + Mascota)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    // Nos traemos los datos del humano
    viewModel: HidratacionViewModel = viewModel(),
    // y los datos de su mascota desde el otro ViewModel
    mascotaViewModel: PerfilMascotaViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val sharedPreferences = remember { context.getSharedPreferences("perfil_preferences", Context.MODE_PRIVATE) }

    // Estados de los campos
    var peso by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var actividad by remember { mutableStateOf("") }

    // Obtenemos de forma segura el clima evitando lecturas concurrentes pesadas
    val climaActual = remember(viewModel.entornoClima) { viewModel.entornoClima }

    // Sincronización con el ViewModel
    // SOLUCIÓN AL CRASHEO Y LENTITUD: Cambiamos las claves de escucha por Unit.
    // Esto asegura que la asignación inicial se haga UNA SOLA VEZ al entrar a la pantalla,
    // previniendo los bucles infinitos de recomposición cuando el usuario borra texto.
    LaunchedEffect(Unit) {
        peso = if (viewModel.usuarioPeso > 0f) viewModel.usuarioPeso.toString() else ""
        edad = if (viewModel.usuarioEdad > 0) viewModel.usuarioEdad.toString() else ""
        genero = viewModel.usuarioGenero.ifEmpty { "" }
        actividad = viewModel.actividadNivel.ifEmpty { "" }
    }

    var expGenero by remember { mutableStateOf(false) }
    var expActividad by remember { mutableStateOf(false) }

    // Manejo de fotos
    var fotoUsuarioUri by remember {
        mutableStateOf<Uri?>(sharedPreferences.getString("saved_profile_uri", null)?.let { Uri.parse(it) })
    }

    // Optimizamos el renderizado de la imagen de la mascota para que no bloquee los recomposes de los textos
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
                sharedPreferences.edit().putString("saved_profile_uri", uri.toString()).apply()
            } catch (e: Exception) {
                fotoUsuarioUri = uri
                sharedPreferences.edit().putString("saved_profile_uri", uri.toString()).apply()
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
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .background(color = Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.guardarPerfil(peso.toFloatOrNull() ?: 70f, edad.toIntOrNull() ?: 25, genero, actividad)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = UserProfileColors.Primary),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
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
            // -------------------------------------------------------------------------------------
            // 1. ACOMODO LADO A LADO (HUMANO Y MASCOTA)
            // -------------------------------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Apartado Humano
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(110.dp)) {
                        AsyncImage(
                            model = fotoUsuarioUri ?: R.drawable.img_hidratacion,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(3.dp, Color.Black, CircleShape)
                                .clickable { galeriaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.label_usuario), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(stringResource(R.string.hint_cambiar_foto), fontSize = 10.sp, color = UserProfileColors.TextHint)
                }

                // Apartado Mascota
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(110.dp)) {
                        AsyncImage(
                            model = fotoMascotaUri ?: R.drawable.img_mascotas_v,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(3.dp, Color.Black, CircleShape)
                                .clickable { navController.navigate("perfil_mascota") },
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.label_mascota), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(stringResource(R.string.hint_ver_perfil), fontSize = 10.sp, color = UserProfileColors.TextHint)
                }
            }

            // -------------------------------------------------------------------------------------
            // 2. TARJETA DE NIVEL Y EXPERIENCIA
            // -------------------------------------------------------------------------------------
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
                    val progreso = remember(viewModel.experienciaNivel, viewModel.experienciaPuntos) {
                        if (viewModel.experienciaNivel > 0) viewModel.experienciaPuntos / (100f * viewModel.experienciaNivel) else 0f
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
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

            // -------------------------------------------------------------------------------------
            // 3. CAMPOS DE CONFIGURACIÓN
            // -------------------------------------------------------------------------------------
            Text(stringResource(R.string.title_config_biometrica), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.fillMaxWidth())

            // Control de decimales optimizado para evitar crasheos de formato regional (comas y puntos)
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
                        focusedBorderColor = UserProfileColors.Primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Box(modifier = Modifier.matchParentSize().clickable { expGenero = true })
                DropdownMenu(expanded = expGenero, onDismissRequest = { expGenero = false }, modifier = Modifier.fillMaxWidth(0.9f)) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.desc_select)) },
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
                        focusedBorderColor = UserProfileColors.Primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Box(modifier = Modifier.matchParentSize().clickable { expActividad = true })
                DropdownMenu(expanded = expActividad, onDismissRequest = { expActividad = false }, modifier = Modifier.fillMaxWidth(0.9f)) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.desc_select)) },
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
                colors = CardDefaults.cardColors(containerColor = UserProfileColors.White.copy(alpha = 0.5f))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.label_entorno_clima), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = climaActual, color = if (climaActual == "Calido") UserProfileColors.ClimaCalido else UserProfileColors.ClimaFrio, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.guardarPerfil(peso.toFloatOrNull() ?: 70f, edad.toIntOrNull() ?: 25, genero, actividad)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UserProfileColors.Primary)
            ) {
                Text(stringResource(R.string.btn_guardar_recalcular), color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}