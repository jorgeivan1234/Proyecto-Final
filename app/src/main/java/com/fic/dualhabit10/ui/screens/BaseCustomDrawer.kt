package com.fic.dualhabit10.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fic.dualhabit10.ui.viewmodels.AuthUiState
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun BaseCustomDrawer(
    navController: NavController,
    drawerState: DrawerState,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false)}
    val uiState by authViewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF9EFFEB),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "DualHabit App",
                        color = Color(0xFFFF7A22),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(20.dp))

                    //boton inicio
                    Button(
                        onClick = {
                            scope.launch { drawerState.close() } //una animacion de cerrar me gusto
                            navController.navigate("habitos") {
                                popUpTo("habitos") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "inicio",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Inicio",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer (modifier = Modifier.height(12.dp))

                    //boton para el perfil
                    Button(
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("perfil")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A22)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "perfil",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "ver perfil",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón para Eliminar Cuenta
                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar cuenta",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Eliminar Cuenta",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    //cerrar sesion
                    Button(
                        onClick = {
                            scope.launch { drawerState.close() }

                            //authViewModel.cerrarSesion() aun no se si usarlo
                            navController.navigate("login"){
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "cerrar sesion",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Cerrar Sesión",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) {  //aquí renderiza la pantalla
        content()

        // lógica para diálogos de confirmación al eliminar cuenta
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Cuenta") },
                text = { Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción es irreversible") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false

                            // si se quiere proceder con la eliminación llamamos a la función del viewmodel y le decimos lo que debe hacer
                            authViewModel.eliminarCuenta(onSuccess = {
                                scope.launch { drawerState.close() }
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            })
                        }
                    ) {
                        Text("Sí, eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar", color = Color.Black)
                    }
                }
            )
        }

        // Lógica para el manejo de datos en firebase
        when (uiState) {
            is AuthUiState.Loading -> {
                // Indicador de carga que confirma el proceso de borrado de cuenta
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {},
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(color = Color(0xFFFF7A22))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Eliminando cuenta de DualHabit...")
                        }
                    }
                )
            }
            is AuthUiState.RequiresReauth -> {
                // En caso de que el usuario haya tenido un tiempo muy largo sin iniciar sesión,
                // firebase requiere de una reautenticación para poder proceder con el borrado
                AlertDialog(
                    onDismissRequest = { authViewModel.resetUiState() },
                    title = { Text("Se requiere reautenticación") },
                    text = { Text("Por seguridad, como ha pasado tiempo desde tu último inicio de sesión, debes cerrar sesión e iniciarla nuevamente antes de poder eliminar tu cuenta") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                authViewModel.resetUiState()
                                scope.launch { drawerState.close() }
                                // Redirección a LoggingScreen para renovar su inicio de sesión
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        ) {
                            Text("Ir a Iniciar Sesión")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { authViewModel.resetUiState() }) {
                            Text("Cancelar", color = Color.Black)
                        }
                    }
                )
            }
            is AuthUiState.Error -> {
                val errorMsg = (uiState as AuthUiState.Error).theMessage
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                authViewModel.resetUiState()
            }
            else -> {}
        }
    }
}