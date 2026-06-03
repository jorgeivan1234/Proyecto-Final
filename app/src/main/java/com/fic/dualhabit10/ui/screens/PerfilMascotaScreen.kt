package com.fic.dualhabit10.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.fic.dualhabit10.ui.viewmodels.PerfilMascotaViewModel

// -------------------------------------------------------------------------------------------------
// Constantes de diseño
// -------------------------------------------------------------------------------------------------

private object ProfileColors {
    val Primary = Color(0xFFFF7A22)
    val Background = Color(0xFF9EFFEB)
    val TextMain = Color.Black
    val TextHint = Color.Gray
    val White = Color.White
}

// -------------------------------------------------------------------------------------------------
// Vista principal del perfil para mascotas
// -------------------------------------------------------------------------------------------------

// Pantalla para registrar o editar la información de una mascota
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMascotaScreen(
    navController: NavController,
    viewModel: PerfilMascotaViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    // Estados posibles a la hora de seleccionar una imagen desde la galería
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_perfil_mascota),
                        fontWeight = FontWeight.Bold,
                        color = ProfileColors.TextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_atras),
                            tint = ProfileColors.TextMain
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = ProfileColors.Primary
                )
            )
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

            // Espacio para la foto de perfil
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(ProfileColors.White, shape = CircleShape)
                    .border(3.dp, ProfileColors.Primary, CircleShape)
                    .clickable { launcher.launch("image/*") },
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

            // Sección donde se define el nombre de la mascota, con agregados de keyboardOptions como:
            // - singleLine: define el apartado como una sola línea de escritura (impide el salto de renglón).
            // - Text: Para hacer que el apartado solo acepte letras, números y bloquee caracteres especiales.
            // - Next: Acción que se encarga de saltar al siguiente apartado.
            // - filtered: filtro que utilizamos para que el usuario solo pueda utilizar letras, números y espacios,
            //   además de filtrar los caracteres, también delimitamos la cantidad a 40 caracteres máximos.
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

            // Apartado de selección para los tipos de especies/mascotas (Utilizando string-array)
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

            // Sección de peso y edad con agregados de keyboardOptions como:
            // - KeyboardType.Number: Obliga a que solo se permita la entrada de números.
            // - ImeAction.Next/.Done: facilita el salto a otro registro y el retiro de teclado una
            //   vez que se hayan llenado los datos.
            // - filtered: Filtro que utilizamos para corregir el uso de "," o el exceso de ". y ,",
            //   además de delimitar el espacio a solo 3 y 2 caracteres máximos.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                OutlinedTextField(
                    value = viewModel.edadMascota,
                    onValueChange = { newValue ->
                        // Filtra caracteres fuera del 0-9
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

            // Sección de botón de guardado de datos
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