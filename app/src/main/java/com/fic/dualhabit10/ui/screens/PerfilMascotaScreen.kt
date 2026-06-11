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
import androidx.compose.material3.MaterialTheme
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
import coil.compose.rememberAsyncImagePainter
import com.fic.dualhabit10.R
import com.fic.dualhabit10.R.array.sexo_mascota
import com.fic.dualhabit10.ui.viewmodels.PerfilMascotaViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.AmarilloFondo

// Vista principal del perfil para mascotas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMascotaScreen(
    navController: NavController,
    viewModel: PerfilMascotaViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(
            if (viewModel.imagenMascota.isNotEmpty()) Uri.parse(viewModel.imagenMascota) else null
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = uri
                viewModel.imagenMascota = uri.toString()
            } catch (e: Exception) {
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
                        color = NaranjaCabecera,
                        shape = RoundedCornerShape(
                            bottomStart = Dimens.cornerRadiusMedium,
                            bottomEnd = Dimens.cornerRadiusMedium
                        )
                    )
                    .statusBarsPadding()
                    .padding(
                        start = Dimens.paddingSmall,
                        end = Dimens.paddingSmall,
                        bottom = Dimens.paddingMedium,
                        top = Dimens.paddingExtraLarge
                    )
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_atras),
                            tint = TextoNegro,
                            modifier = Modifier.size(Dimens.iconSizeLarge)
                        )
                    }

                    Text(
                        text = stringResource(R.string.title_perfil_mascota),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextoNegro,
                        modifier = Modifier
                            .background(
                                color = AmarilloFondo,
                                shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                            )
                            .padding(
                                horizontal = Dimens.paddingMedium,
                                vertical = Dimens.paddingTiny
                            )
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(VerdeFondoHabitos)
                .verticalScroll(scrollState)
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(
                modifier = Modifier
                    .size(Dimens.imageProfileSizeLarge)
                    .background(TextoBlanco, shape = CircleShape)
                    .border(Dimens.borderThickness, NaranjaCabecera, CircleShape)
                    .clickable {
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
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape), // Evita que la imagen sobresalga del borde circular
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = stringResource(R.string.btn_add_foto),
                        color = GrisTextoHint,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.spacerMedium))

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
                shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(Dimens.spacerSmall))

            Text(
                text = stringResource(R.string.title_especie),
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingTiny)
            ) {
                stringArrayResource(R.array.especies_mascota).forEach { especie ->
                    FilterChip(
                        selected = viewModel.especieMascota == especie,
                        onClick = { viewModel.especieMascota = especie },
                        label = { Text(especie) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.spacerSmall))

            Text(
                text = stringResource(R.string.label_sexo_mascota),
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingTiny)
            ) {
                stringArrayResource(sexo_mascota).forEach { sexo ->
                    FilterChip(
                        selected = viewModel.sexoMascota == sexo,
                        onClick = { viewModel.sexoMascota = sexo },
                        label = { Text(sexo) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.spacerSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
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
                    shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

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
                    shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.spacerLarge))

            Button(
                onClick = {
                    viewModel.guardarDatos {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.buttonHeight),
                colors = ButtonDefaults.buttonColors(containerColor = NaranjaCabecera),
            ) {
                Text(
                    text = stringResource(R.string.btn_guardar_mascota),
                    color = TextoNegro,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}