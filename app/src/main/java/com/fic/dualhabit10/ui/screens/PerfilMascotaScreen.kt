package com.fic.dualhabit10.ui.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fic.dualhabit10.R
import com.fic.dualhabit10.R.array.sexo_mascota
import com.fic.dualhabit10.ui.viewmodels.PerfilMascotaViewModel

// Constantes de diseño
private object ProfileColors {
    val Primary = Color(0xFFFF7A22)
    val Background = Color(0xFF9EFFEB)
    val TextMain = Color.Black
    val TextHint = Color.Gray
    val White = Color.White
}

// Vista principal del perfil para mascotas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMascotaScreen(
    navController: NavController,
    viewModel: PerfilMascotaViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    // Contexto requerido para la persistencia del flujo de permisos de archivos locales
    val context = LocalContext.current

    // Carga la URI previa si existe o inicia limpia en null sin errores
    var imageUri by remember {
        mutableStateOf<Uri?>(
            if (viewModel.imagenMascota.isNotEmpty()) Uri.parse(viewModel.imagenMascota) else null
        )
    }

    // Selector multimedia moderno que solicita permisos de lectura de larga duración
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                // Forzamos a Android a mantener el acceso al archivo incluso tras reiniciar el dispositivo
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = uri
                viewModel.imagenMascota = uri.toString()
            } catch (e: Exception) {
                // Respaldo inmediato en caso de restricciones de seguridad del sistema operativo
                imageUri = uri
                viewModel.imagenMascota = uri.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        ProfileColors.Primary,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 28.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Botón de salida posicionado a la izquierda del contenedor principal
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_atras),
                            tint = ProfileColors.TextMain,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.title_perfil_mascota),
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ProfileColors.Background)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Contenedor circular responsivo para la foto de la mascota
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(ProfileColors.White, shape = CircleShape)
                    .border(3.dp, ProfileColors.Primary, CircleShape)
                    .clickable {
                        // Lanzador nativo restringido exclusivamente a imágenes de galería
                        launcher.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = stringResource(R.string.desc_foto_mascota),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = stringResource(R.string.btn_add_foto),
                        color = ProfileColors.TextHint,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de texto controlado con filtrado alfanumérico estricto para el nombre
            OutlinedTextField(
                value = viewModel.nombreMascota,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isLetterOrDigit() || it.isWhitespace() }

                    if (filtered.length <= 40) {
                        viewModel.nombreMascota = filtered
                    }
                },
                label = { Text(stringResource(R.string.label_nombre_mascota)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Selector horizontal de chips para clasificar la especie animal
            Text(
                text = stringResource(R.string.title_especie),
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stringArrayResource(R.array.especies_mascota).forEach { especie ->
                    FilterChip(
                        selected = viewModel.especieMascota == especie,
                        onClick = { viewModel.especieMascota = especie },
                        label = { Text(especie) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Selector horizontal de chips para clasificar el sexo de la mascota
            Text(
                text = stringResource(R.string.label_sexo_mascota), // <-- Cambiado de 'id= sexo_mascota' a 'R.string.label_sexo_mascota'
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stringArrayResource(sexo_mascota).forEach { sexo ->
                    FilterChip(
                        selected = viewModel.sexoMascota == sexo,
                        onClick = { viewModel.sexoMascota = sexo },
                        label = { Text(sexo) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Fila de parámetros físicos de la mascota con sanitización de entradas métricas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Campo de peso con normalización automática de separadores decimales
                OutlinedTextField(
                    value = viewModel.pesoMascota,
                    onValueChange = { newValue ->
                        val filtered = newValue.replace(',', '.').filter { it.isDigit() || it == '.' }

                        if (filtered.count { it == '.' } <= 1 && filtered.length <= 3) {
                            viewModel.pesoMascota = filtered
                        }
                    },
                    label = { Text(stringResource(R.string.label_peso)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                // Campo de edad con restricción física a un máximo de dos dígitos cronológicos
                OutlinedTextField(
                    value = viewModel.edadMascota,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }

                        if (filtered.length <= 2) {
                            viewModel.edadMascota = filtered
                        }
                    },
                    label = { Text(stringResource(R.string.label_edad)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            // Botón de guardado que direcciona los datos a la base de datos local
            Button(
                onClick = {
                    viewModel.guardarDatos {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ProfileColors.Primary),
            ) {
                Text(
                    text = stringResource(R.string.btn_guardar_mascota),
                    color = ProfileColors.TextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}