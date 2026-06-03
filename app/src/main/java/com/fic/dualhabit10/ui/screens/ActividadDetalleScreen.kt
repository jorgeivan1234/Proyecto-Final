package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.ui.viewmodels.ActividadFisicaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadDetalleScreen(
    actividadId: Int,
    navController: NavController,
    viewModel: ActividadFisicaViewModel = viewModel()
) {
    //busca la actividad por ID
    val actividad by viewModel.buscarActividadPorId(actividadId).collectAsState(initial = null)

    actividad?.let { data ->
        val colorBarra = try {
            Color(android.graphics.Color.parseColor(data.colorHex))
        } catch (e: Exception) {
            Color(0xFFFF7A22)
        }

        val colorFondoPantalla = Color(0xFFBFF7E8)
        Scaffold(
            containerColor = colorFondoPantalla,
            topBar = {
                TopAppBar(
                    title = { Text("Detalle de Rutina", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = colorBarra)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorFondoPantalla)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // imagen real
                AsyncImage(
                    model = data.imagenUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
                // contenedor de la info del ejercicio
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = data.titulo,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text(text = "Duracion: ${data.duracion}") },
                        )
                        SuggestionChip(
                            onClick = { },
                            label = { Text(text = "Intensidad: ${data.intensidad}") },
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "Instrucciones del Ejercicio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = data.descripcion,
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    //tarjeta de sugerencia
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorBarra.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "¡Recuerda llevar suficiente agua para ti y tu mascota durante el entrenamiento!",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}