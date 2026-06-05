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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.fic.dualhabit10.ui.viewmodels.PerfilMascotaViewModel

// -------------------------------------------------------------------------------------------------
// Paleta de colores de la pantalla
// -------------------------------------------------------------------------------------------------
// Nuestros colores de siempre para que esta pantalla no desentone con la vibra de la app
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

    // Usamos SharedPreferences para que la foto del humano sobreviva aunque cierren la app de golpe
    val sharedPreferences = remember {
        context.getSharedPreferences("perfil_preferences", Context.MODE_PRIVATE)
    }

    // Variables amarradas a lo que ya calculamos en el ViewModel
    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString()) }
    var edad by remember { mutableStateOf(viewModel.usuarioEdad.toString()) }
    val climaActual = viewModel.entornoClima
    var genero by remember { mutableStateOf(viewModel.usuarioGenero) }
    var actividad by remember { mutableStateOf(viewModel.actividadNivel) }

    // Para saber si los menús de abajo están abiertos o cerrados
    var expGenero by remember { mutableStateOf(false) }
    var expActividad by remember { mutableStateOf(false) }

    // Intentamos rascar la foto del humano desde el SharedPreferences
    var fotoUsuarioUri by remember {
        mutableStateOf<Uri?>(
            sharedPreferences.getString("saved_profile_uri", null)?.let { Uri.parse(it) }
        )
    }

    // Agarramos la foto de la mascota directamente de su ViewModel de forma segura
    // Si hay foto, la convertimos en Uri para poder pintarla, si no, le dejamos un null pacífico
    val fotoMascotaUri = remember(mascotaViewModel.imagenMascota) {
        if (mascotaViewModel.imagenMascota.isNotEmpty()) Uri.parse(mascotaViewModel.imagenMascota) else null
    }
    // takePersistableUriPermission nos ayuda a poder reutilizar la foto cuanto queramos
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        UserProfileColors.Primary,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
                    // Empujamos todo exactamente 28.dp hacia abajo para que quede perfecto
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 28.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            viewModel.guardarPerfil(
                                peso.toFloatOrNull() ?: 70f,
                                edad.toIntOrNull() ?: 25,
                                genero,
                                actividad
                            )
                            navController.popBackStack()
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_atras),
                            tint = UserProfileColors.TextMain,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.title_perfil_compartido),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFF200),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
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

            // Fila principal donde ponemos al humano y a la mascota lado a lado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Apartado para el humano
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (fotoUsuarioUri != null) {
                        // Coil hace la chamba pesada de cargar la foto real desde el cel
                        AsyncImage(
                            model = fotoUsuarioUri,
                            contentDescription = stringResource(R.string.desc_foto_usuario),
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .border(3.dp, UserProfileColors.TextMain, CircleShape)
                                .clickable {
                                    // Para entrar a la galería
                                    galeriaLauncher.launch(
                                        androidx.activity.result.PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Si no hay foto, ponemos la imagen por defecto
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

                // Apartado de mascota
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (fotoMascotaUri != null) {
                        AsyncImage(
                            model = fotoMascotaUri,
                            contentDescription = stringResource(R.string.desc_foto_mascota),
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .border(3.dp, UserProfileColors.TextMain, CircleShape)
                                .clickable {
                                    // Redirección a perfil de mascota
                                    navController.navigate("perfil_mascota")
                                },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Si no hay fotos agregadas, ponemos las por defecto
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
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.label_mascota), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(stringResource(R.string.hint_ver_perfil), fontSize = 10.sp, color = UserProfileColors.TextHint)
                }
            }

            // Apartado del nivel de la cuenta
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

            // Filtros para impedir que se rompa la app y hacer más fácil la lectura de los datos
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

            // Delimitación de caracteres la edad permitidos para mascotas
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

            // Apartado de menú desplegable para elegir el sexo biologico
            ExposedDropdownMenuBox(
                expanded = expGenero,
                onExpandedChange = { expGenero = !expGenero }
            ) {
                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    readOnly = true, // Que no escriban, ¡que seleccionen!
                    label = { Text(stringResource(R.string.label_genero)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expGenero) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expGenero,
                    onDismissRequest = { expGenero = false }
                ){
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.desc_select)) },
                            onClick = { genero = "Seleccionar"; expGenero = false }
                    )

                    stringArrayResource(R.array.generos_biologicos).forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { genero = opcion; expGenero = false }
                        )
                    }
                }
            }

            // Lo mismo pero para el nivel de Actividad
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
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.desc_select)) },
                        onClick = { actividad = "Seleccionar"; expActividad = false }
                    )

                    stringArrayResource(R.array.niveles_actividad).forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { actividad = opcion; expActividad = false }
                        )
                    }
                }
            }

            // Tarjeta transparente que utiliza la API del clima
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
                        // Apartado de colores según la temperatura actual
                        color = if (climaActual == "Calido") UserProfileColors.ClimaCalido else UserProfileColors.ClimaFrio,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Guarda los datos y nos redirige
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