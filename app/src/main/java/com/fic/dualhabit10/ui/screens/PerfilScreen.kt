package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.ActivityNavigatorExtras
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import kotlin.math.exp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.ActivityNavigatorExtras
import coil.compose.AsyncImage
import com.fic.dualhabit10.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel(),
){
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    //para que no se pierda la foto de perfil
    val sharedPreferences = remember {
        context.getSharedPreferences("perfil_preferences", Context.MODE_PRIVATE)
    }
    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString() )}
    var edad by remember { mutableStateOf(viewModel.usuarioEdad.toString() )}
    var clima by remember { mutableStateOf(viewModel.entornoClima)}
    var genero by remember  { mutableStateOf(viewModel.usuarioGenero)}
    var actividad by remember  { mutableStateOf(viewModel.actividadNivel)}

    var expGenero by remember { mutableStateOf(false)}
    var expActividad by remember { mutableStateOf(false)}

    //recupera la foto
    var fotoUsuarioUri by remember {
        mutableStateOf<Uri?>(
            sharedPreferences.getString("saved_profile_uri", null)?.let { Uri.parse(it) }
        )
    }

    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString() )}
    var edad by remember { mutableStateOf(viewModel.usuarioEdad.toString() )}
    var clima by remember { mutableStateOf(viewModel.entornoClima)}
    var fotoUsuarioUri by remember { mutableStateOf<Uri?>(null) }
    var galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        //si el usuario selecciona una imgen, guardamos su ruta
        if (uri != null) {
            try {
                val intent = null
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                fotoUsuarioUri = uri
                //guarda la ruta
                sharedPreferences.edit().putString("saved_profile_uri", uri.toString()).apply()
            } catch (e: Exception) {
                fotoUsuarioUri = uri
                sharedPreferences.edit().putString("saved_profile_uri", uri.toString()).apply()
            }
        }
    }

        if (uri != null){
            fotoUsuarioUri = uri
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil Compartido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.guardarPerfil(
                            peso.toFloatOrNull() ?: 70f,
                            edad.toIntOrNull() ?: 25,
                            genero,
                            actividad,
                            "Masculino",
                            "Moderado",
                            clima
                        )
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras")
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
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    if(fotoUsuarioUri != null) {
                        AsyncImage(
                            model = fotoUsuarioUri,
                            contentDescription = "Foto elegida por el usuario",
                            modifier = Modifier
                                .size(110.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color.Black, CircleShape)
                                .clickable {
                                    //al pulsar la foto abre galeria
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
                            contentDescription = "Foto Humano",
                            modifier = Modifier
                                .size(110.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color.Black, CircleShape)
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
                    Text("Usuario", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Pulsame para cambiar", fontSize = 10.sp, color = Color.Gray)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mascotas_v),
                        contentDescription = "Foto mascota",
                        modifier = Modifier
                            .size(110.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.Black, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Mascota", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("", fontSize = 10.sp)
                    Text("Mascota", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            //experiencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Nivel de Cuenta: ${viewModel.experienciaNivel}",
                        color = Color.Yellow,
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
                        color = Color(0xFF29B6F6)
                    )
                    Text(
                        "Puntos de Experiencia para el siguiente logro",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Text(
                "Confirguracion de parametros Biometricos",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
                Column(modifier = Modifier.padding(16.dp)){
                    Text("Nivel de Cuenta: ${viewModel.experienciaNivel}", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    LinearProgressIndicator(
                        progress = viewModel.experienciaPuntos / (100f * viewModel.experienciaNivel),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        color = Color(0xFF29B6F6)
                    )
                    Text("Puntos de Experiencia para el siguiente logro", color= Color.White, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            Text("Confirguracion de parametros Biometricos", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                label = { Text("Peso actual (Kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expGenero,
                onExpandedChange = { expGenero = !expGenero }
            ) {
                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Genero Biologico") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expGenero) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expGenero,
                    onDismissRequest = { expGenero = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Masculino") },
                        onClick = { genero = "Masculino"; expGenero = false })
                    DropdownMenuItem(
                        text = { Text("Femenino") },
                        onClick = { genero = "Femenino"; expGenero = false })
                }
            }

            ExposedDropdownMenuBox(
                expanded = expActividad,
                onExpandedChange = { expActividad = !expActividad }
            ) {
                OutlinedTextField(
                    value = actividad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nivel de Actividad Fisica") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expActividad) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expActividad,
                    onDismissRequest = { expActividad = false }
                ) {
                    DropdownMenuItem(text = { Text("Sedentario") }, onClick = { actividad = "Sedentario"; expActividad = false})
                    DropdownMenuItem(text = { Text("Moderado") }, onClick = { actividad = "Moderado"; expActividad = false})
                    DropdownMenuItem(text = { Text("Intenso") }, onClick = { actividad = "Intenso"; expActividad = false})
                }
            }
            OutlinedTextField(
                value = clima,
                onValueChange = { clima = it },
                label = { Text("Entorno Meteorologico (Calido / Frio)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.guardarPerfil(
                        peso.toFloatOrNull() ?: 70f,
                        edad.toIntOrNull() ?: 25,
                        genero,
                        actividad,
                        "Masculino",
                        "Moderado",
                        clima
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22))
            ) {
                Text("Guardar y Recalcular Meta", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}