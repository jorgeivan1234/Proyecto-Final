package com.fic.dualhabit10.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.data.local.SuenoEntity
import com.fic.dualhabit10.ui.viewmodels.SuenoViewModel
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantenimientoSuenoScreen(
    navController: NavController,
    viewModel: SuenoViewModel = viewModel()
) {
    // Leemos constantemente el ViewModel para saber si el cronómetro está activo
    val estaDurmiendo = viewModel.estaDurmiendo
    val tiempoMs = viewModel.tiempoTranscurridoMs
    // collectAsState permite que la lista del historial se actualice automáticamente en la interfaz
    val historial by viewModel.historialSueno.collectAsState()

    // Controla si se muestra o se oculta el diálogo de evaluación de energía matutina
    var mostrarDialogoAnimo by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Matemáticas básicas para convertir los milisegundos de la base de datos en horas, minutos y segundos
    val segundos = (tiempoMs / 1000) % 60
    val minuto = (tiempoMs / (1000 * 60)) % 60
    val hora = (tiempoMs / (1000 * 60 * 60)) % 24
    // Formato estándar de reloj digital
    val tiempoText = String.format("%02d:%02d:%02d", hora, minuto, segundos)

    // Cargamos la lista de consejos desde el archivo de recursos Strings para permitir su traducción
    val tipsMascotas = stringArrayResource(R.array.tips_mascotas_sueno)

    // Seleccionamos un consejo al azar solo una vez cuando la pantalla se dibuja
    val tipAleatorio = remember { tipsMascotas.random() }

    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFFF7A22),
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .statusBarsPadding()
                        .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 28.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.desc_menu),
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Text(
                            text = stringResource(R.string.title_modo_sueno),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
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
        ) { padding ->
            // Fondo general de la pantalla
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B263B), Color(0xFF415A77))
                        )
                    )
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Indicador visual principal (Luna si está durmiendo, Sol si está despierto)
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(Color.White.copy(alpha = 0.08f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (estaDurmiendo) Icons.Default.Bedtime else Icons.Default.WbSunny,
                        contentDescription = stringResource(R.string.desc_estado_sueno),
                        tint = if (estaDurmiendo) Color(0xFF90CAF9) else Color(0xFFFFD54F),
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Zona de cronómetro o mensaje de bienvenida
                Text(
                    text = if (estaDurmiendo) tiempoText else stringResource(R.string.text_listo_descansar),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (estaDurmiendo) stringResource(R.string.text_registrando_sueno) else stringResource(R.string.text_presiona_dormir),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón principal que intercala entre iniciar el cronómetro o abrir el diálogo para finalizar
                Button(
                    onClick = {
                        if (estaDurmiendo) {
                            mostrarDialogoAnimo = true // Abre la ventana emergente al despertar
                        } else {
                            viewModel.iniciarDescanso() // Activa el conteo en base de datos
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        // Rojo para detener, Naranja para arrancar
                        containerColor = if (estaDurmiendo) Color(0xFFE63946) else Color(0xFFFF7A22)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = if (estaDurmiendo) stringResource(R.string.btn_despertar) else stringResource(R.string.btn_iniciar_descanso),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Sección del historial que se oculta dinámicamente si no hay registros en la base de datos
                AnimatedVisibility(visible = historial.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.title_ultimas_noches),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Lista horizontal desplazable para ver los registros anteriores
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(historial) { registro ->
                                TarjetaHistorialSueno(registro)
                            }
                        }
                        Spacer(modifier = Modifier.height(36.dp))
                    }
                }

                // Tarjeta con un consejo sobre el sueño de las mascotas
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(
                                        Color(0xFFFF7A22).copy(alpha = 0.2f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = stringResource(R.string.desc_tips_mascota),
                                    tint = Color(0xFFFF7A22),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = stringResource(R.string.title_descanso_mascota),
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Muestra el tip aleatorio que seleccionamos al principio de la vista
                        Text(
                            text = tipAleatorio,
                            color = Color.LightGray,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    // Diálogo flotante que captura la calidad del sueño (estado de ánimo) justo al despertar
    if (mostrarDialogoAnimo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAnimo = false },
            title = {
                Text(
                    text = stringResource(R.string.dialog_title_despertar),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.dialog_desc_despertar),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Selecciona un emoji termina el contador y guarda el registro
                    listOf("😫", "😐", "😊", "⚡").forEach { emoji ->
                        TextButton(
                            onClick = {
                                viewModel.terminarDescanso(emoji)
                                mostrarDialogoAnimo = false
                            }
                        ) {
                            Text(text = emoji, fontSize = 34.sp)
                        }
                    }
                }
            },
            shape = RoundedCornerShape(28.dp),
        )
    }
}

// Componentes Visuales Reutilizables
@Composable
fun TarjetaHistorialSueno(registro: SuenoEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.width(115.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Se muestra el emoji guardado para identificar rápidamente si fue una buena o mala noche
            Text(text = registro.estadoAnimo, fontSize = 28.sp)
            Text(
                // Inyectamos el valor numérico en el recurso de texto
                text = stringResource(R.string.text_horas_dormidas, registro.horasDormidas),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )
            Text(
                text = registro.fecha,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}