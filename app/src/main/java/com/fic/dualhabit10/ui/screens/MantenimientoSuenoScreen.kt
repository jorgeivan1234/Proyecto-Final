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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.data.local.SuenoEntity
import com.fic.dualhabit10.ui.viewmodels.SuenoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantenimientoSuenoScreen(
    navController: NavController,
    viewModel: SuenoViewModel = viewModel()
) {

    val estaDurmiendo = viewModel.estaDurmiendo
    val tiempoMs = viewModel.tiempoTranscurridoMs
    val historial by  viewModel.historialSueno.collectAsState()
    var mostrarDialogoAnimo by remember {mutableStateOf(false)}
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val segundos = (tiempoMs / 1000) % 60
    val minuto = (tiempoMs / (1000 * 60)) % 60
    val hora = (tiempoMs / (1000 * 60 * 60)) % 24
    val tiempoText = String.format("%02d:%02d:%02d", hora, minuto, segundos)

    val tipsMascotas = remember {
        listOf(
            "Los perros adultos duermen entre 12 y 14 horas al dia. ¡Asegurate de que su espacio sea comodo!",
            "Un sueño inquieto en tu mascota puede indicar que necesita mas actividad fusuca el dia .",
            "Los cachorros pueden dormir hasta 18 horas diarias para apoyar su rapido crecimiento oseo",
            "Evita que tu mascota duerma frente a corrientes directas de aire o aires acondicionados muy frios."
        )
    }
    val tipAleatorio = remember { tipsMascotas.random() }

    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Modo Sueño",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    },

                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                    },
                    modifier = Modifier
                        .height(85.dp)
                        .background(
                            Color(0xFFFF7A22),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        ),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
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

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(Color.White.copy(alpha = 0.08f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                )  {
                    Icon(
                        imageVector = if (estaDurmiendo) Icons.Default.Bedtime else Icons.Default.WbSunny,
                        contentDescription = "Estado",
                        tint = if (estaDurmiendo) Color(0xFF90CAF9) else Color(0xFFFFD54F),
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer (modifier = Modifier.height(24.dp))
                //cronometro
                Text(
                    text = if (estaDurmiendo) tiempoText else "¡Listo para descansar!",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (estaDurmiendo) "Registrando horas de sueño tuyas y de tu mascota ..." else "Presiona el boton para al ir a dormir.",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier .padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                //boton de control principal
                Button(
                    onClick = {
                        if (estaDurmiendo) {
                            mostrarDialogoAnimo = true
                        } else {
                            viewModel.iniciarDescanso()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estaDurmiendo) Color(0xFFE63946) else Color(0xFFFF7A22)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = if (estaDurmiendo) "Despertar" else "Iniciar Descanso",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                //sesion historial
                AnimatedVisibility(visible = historial.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Tus ultimas noches",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
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

                //sesion mascota
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
                                    contentDescription = "Tips Mascota",
                                    tint = Color(0xFFFF7A22),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Descanso de tu Mascota",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

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

    if (mostrarDialogoAnimo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAnimo = false },
            title = {
                Text(
                    text = "¡Buenos dias!\n ¿Como descansaron hoy?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Selecciona el emoji que mejor represente tu energia y la de tu mascota esta mañana.",
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
            Text(text = registro.estadoAnimo, fontSize = 28.sp)
            Text(
                text = "${registro.horasDormidas} hrs",
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
