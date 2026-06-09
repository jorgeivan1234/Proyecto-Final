package com.fic.dualhabit10.ui.screens

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
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
                        IconButton(onClick = {
                            viewModel.pausarOdetenerJuego()
                            navController.popBackStack()
                        }) {
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

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, colorBarra, RoundedCornerShape(20.dp)),
                        colors= CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Modulo interactivo de reto",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Color.Gray
                            ) //caso 1 cronometro en marcha
                            if (viewModel.juegoEnProgreso){
                                val min = viewModel.tiempoRestanteKey / 60
                                val seg = viewModel.tiempoRestanteKey % 60
                                Text(
                                    text = String.format("%02d:%02d", min, seg),
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFF5252)
                                )
                                Text("¡Haz la actividad con tu mascota ahora!", fontSize = 13.sp, color = Color.Gray)

                                Button(
                                    onClick = { viewModel.pausarOdetenerJuego() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                                ){
                                    Icon(Icons.Default.Pause, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Cancelar Entrenamiento")
                                }
                            }
                            //caso 2 felicidade por terminar :)
                            else if (viewModel.juegosCompletados) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(48.dp)
                                )

                                Text(
                                    text = "¡Reto Completado!",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2ECC71)
                                )

                                Text(
                                    text = "Has acomulado +$viewModel.puntosGanados} Puntos de felicidad. El registro se guardo exitosamente en tu bitacora de paseos.",
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                )

                                Button(
                                    onClick = {
                                        val minutosLimpios = data.duracion.replace(" min", "").trim().toIntOrNull() ?: 15
                                        viewModel.iniciarJuego(minutosLimpios)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = colorBarra)
                                ) {
                                    Text("Jugar de Nuevo", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                            //caso 3
                            else {
                                Text(
                                    text = "Presiona inciar para cronometrar tu tiempo real y sumar puntos.",
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )

                                Button(
                                    onClick = {
                                        val minutosLimpios = data.duracion.replace(" min", "").trim().toIntOrNull() ?: 15
                                        viewModel.iniciarJuego(minutosLimpios)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = colorBarra)
                                ) {
                                    Icon(Icons.Default.PlayArrow,contentDescription = null, tint = Color.Black)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Iniciar Reto Real", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
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