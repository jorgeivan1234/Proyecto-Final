package com.fic.dualhabit10.ui.screens

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
    var peso by remember { mutableStateOf(viewModel.usuarioPeso.toString() )}
    var edad by remember { mutableStateOf(viewModel.usuarioEdad.toString() )}
    var clima by remember { mutableStateOf(viewModel.entornoClima)}
    var fotoUsuarioUri by remember { mutableStateOf<Uri?>(null) }
    var galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        //si el usuario selecciona una imgen, guardamos su ruta
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    if(fotoUsuarioUri != null) {
                        AsyncImage(
                            model = fotoUsuarioUri,
                            contentDescription = "Foto elegida por el usuario",
                            modifier = Modifier
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
                    Text("Usuario", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Pulsame para cambiar", fontSize = 10.sp, color = Color.Gray)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mascotas_v),
                        contentDescription = "Foto mascota",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.Black, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text("Mascota", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            //experiencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
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

            OutlinedTextField(
                value = clima,
                onValueChange = { clima = it },
                label = { Text("Entorno Meteorologico (Calido / Frio)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.guardarPerfil(
                        peso.toFloatOrNull() ?: 70f,
                        edad.toIntOrNull() ?: 25,
                        "Masculino",
                        "Moderado",
                        clima
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22))
            ) {
                Text("Guardar y Recalcular Meta", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}