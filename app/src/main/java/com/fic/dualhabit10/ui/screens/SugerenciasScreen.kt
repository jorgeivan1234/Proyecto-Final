package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.SugerenciasViewModel
import kotlinx.coroutines.launch

@Composable
fun Sugerencias(
    navController: NavHostController,
    viewModel: SugerenciasViewModel
) {
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
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 28.dp)
            ) {

                // Cambiamos el Row por un Box para lograr un centrado absoluto
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Botón para abrir el menú lateral, anclado a la izquierda
                    IconButton(
                        onClick = { scoper.launch { drawerState.open() } },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.desc_menu),
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Título de la pantalla con fondo amarillo, anclado al centro por defecto en este Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.title_sugerencias),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
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
                    text = stringResource(R.string.title_buzon_sugerencias),
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
                    label = { Text(stringResource(R.string.hint_escribe_comentario)) },
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
                            viewModel.guardarSugerencias(textoLimpio)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A237E), // Azul oscuro
                        contentColor = Color.White // Color del texto dentro del botón
                    )
                ) {
                    Text(stringResource(R.string.btn_enviar_sugerencia), fontSize = 16.sp)
                }
            }
        }
    }
}