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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun Sugerencias(navController: NavHostController) {
    var sugerenciaTexto by remember { mutableStateOf("") }
    val limiteCaracteres = 500

    // 1. Estado y scope necesarios para que el menú lateral funcione
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scoper = rememberCoroutineScope()

    // Envolvemos toda la pantalla en el BaseCustomDrawer
    BaseCustomDrawer(navController = navController, drawerState = drawerState) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
        ) {

            //BARRA DE NAVEGACIÓN SUPERIOR
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFFF7A22), // Mismo naranja de HabitosScreen
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón para abrir el menú lateral
                    IconButton(
                        onClick = { scoper.launch { drawerState.open() } }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Título de la pantalla en la barra superior
                    Text(
                        text = "Sugerencias",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            //El formulario de sugerencias
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Buzón de Sugerencias",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Black
                )

                TextField(
                    value = sugerenciaTexto,
                    onValueChange = { nuevoTexto ->
                        if (nuevoTexto.length <= limiteCaracteres) {
                            sugerenciaTexto = nuevoTexto
                        }
                    },
                    label = { Text("Escribe tu comentario aquí...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${sugerenciaTexto.length} / $limiteCaracteres",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End),
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (sugerenciaTexto.isNotBlank()) {
                            val textoLimpio = sugerenciaTexto.trim()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A237E), // Azul oscuro (puedes cambiar este código Hexadecimal)
                        contentColor = Color.White // Color del texto dentro del botón
                    )
                ) {
                    Text("Enviar sugerencia", fontSize = 16.sp)
                }
            }
        }
    }
}