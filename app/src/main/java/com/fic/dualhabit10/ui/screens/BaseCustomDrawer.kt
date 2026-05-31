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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AuthUiState
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

// -------------------------------------------------------------------------------------------------
// Constantes y Configuración Visual
// -------------------------------------------------------------------------------------------------

// Centralización de colores utilizados en el menú lateral
private object AppColors {
    val DrawerBackground = Color(0xFF9EFFEB)
    val PrimaryBtn = Color(0xFFFF7A22)
    val DangerDarkBtn = Color(0xFF8B0000)
    val DangerBtn = Color(0xFFD32F2F)
}

// Centralización de las rutas de navegación para evitar errores tipográficos al momento de usar el NavController
private object AppRoutes {
    const val HABITOS = "habitos"
    const val PERFIL = "perfil"
    const val LOGIN = "login"
}

// -------------------------------------------------------------------------------------------------
// Vista Principal: Contenedor del menú lateral
// -------------------------------------------------------------------------------------------------

// BaseCustomDrawer hace posible que la pantalla principal de la aplicación pueda ser dotada
// de un menú lateral funcional
// @param navController Controlador para navegar entre las diferentes pantallas.
// @param drawerState Estado actual del menú (abierto o cerrado).
// @param authViewModel ViewModel inyectado para manejar la lógica de sesión en Firebase.
// @param content Función composable que renderiza la pantalla principal (el contenido central).

@Composable
fun BaseCustomDrawer(
    navController: NavController,
    drawerState: DrawerState,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    content: @Composable () -> Unit
) {
    // Contextos y estados de ciclo de vida necesarios
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Estado local para mostrar/ocultar la ventana de confirmación de eliminación
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Estado reactivo que escucha las respuestas de Firebase desde el ViewModel
    val uiState by authViewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = AppColors.DrawerBackground,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
            ) {
                // Contenedor principal de los elementos del menú
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Título y separador del menú
                    Text(
                        text = stringResource(R.string.app_title),
                        color = AppColors.PrimaryBtn,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(20.dp))

                    // Detalles de la opción Inicio
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_home),
                        icon = Icons.Default.Home,
                        containerColor = AppColors.PrimaryBtn,
                        contentColor = Color.Black
                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppRoutes.HABITOS) {
                            // Limpieza de la pila de navegación
                            popUpTo(AppRoutes.HABITOS) { inclusive = false }
                            launchSingleTop = true
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Detalles de la opción Perfil
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_profile),
                        icon = Icons.Default.AccountCircle,
                        containerColor = AppColors.PrimaryBtn,
                        contentColor = Color.Black
                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppRoutes.PERFIL)
                    }

                    // Empuja los botones de abajo hacia el final de la pantalla
                    Spacer(modifier = Modifier.weight(1f))

                    // Detalles de la opción Eliminar Cuenta
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_delete_account),
                        icon = Icons.Default.Delete,
                        containerColor = AppColors.DangerDarkBtn,
                        contentColor = Color.White
                    ) {
                        // Abre el recuadro de advertencia
                        showDeleteDialog = true
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Detalles de la opción Cerrar Sesión
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_logout),
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        containerColor = AppColors.DangerBtn,
                        contentColor = Color.White
                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppRoutes.LOGIN) {
                            // Elimina el historial de pantallas para que el usuario no puedan volver
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        }
    ) {
        // Aquí se renderiza la pantalla
        content()

        // -----------------------------------------------------------------------------------------
        // Lógica de diálogos y estados
        // -----------------------------------------------------------------------------------------

        // Cuadro de confirmación inicial para eliminar cuenta
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(R.string.dialog_delete_title)) },
                text = { Text(stringResource(R.string.dialog_delete_body)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            // Petición de eliminación y lógica de borrado si procede
                            authViewModel.eliminarCuenta(onSuccess = {
                                scope.launch { drawerState.close() }
                                navController.navigate(AppRoutes.LOGIN) {
                                    popUpTo(0) { inclusive = true }
                                }
                            })
                        }
                    ) {
                        Text(stringResource(R.string.dialog_btn_yes), color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(stringResource(R.string.dialog_btn_cancel), color = Color.Black)
                    }
                }
            )
        }

        // Manejador del estado actual devuelto por Firebase
        when (uiState) {
            is AuthUiState.Loading -> {
                // Indicador visual para que el usuario sepa que hay un proceso ejecutándose en segundo plano
                AlertDialog(
                    onDismissRequest = {}, // Evita que se cierre tocando fuera
                    confirmButton = {},
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(color = AppColors.PrimaryBtn)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(R.string.dialog_loading))
                        }
                    }
                )
            }
            is AuthUiState.RequiresReauth -> {
                // Alerta que salta cuando Firebase detecta que la sesión es muy antigua
                AlertDialog(
                    onDismissRequest = { authViewModel.resetUiState() },
                    title = { Text(stringResource(R.string.dialog_reauth_title)) },
                    text = { Text(stringResource(R.string.dialog_reauth_body)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                authViewModel.resetUiState()
                                scope.launch { drawerState.close() }
                                // Redirige al login para renovar la sesión
                                navController.navigate(AppRoutes.LOGIN) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        ) {
                            Text(stringResource(R.string.dialog_btn_goto_login))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { authViewModel.resetUiState() }) {
                            Text(stringResource(R.string.dialog_btn_cancel), color = Color.Black)
                        }
                    }
                )
            }
            is AuthUiState.Error -> {
                // Muestra errores de red o excepciones devueltas por el servidor en formato Toast
                val errorMsg = (uiState as AuthUiState.Error).theMessage
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                authViewModel.resetUiState()
            }
            else -> {} // Estado Idle o Success
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Componentes reutilizables
// -------------------------------------------------------------------------------------------------

// DrawerMenuButton: Crea un botón pre-diseñado específicamente para el menú lateral
// @param text - Texto a mostrar en el botón
// @param icon - Icono vectorizado que acompaña al texto
// @param containerColor - Color de fondo del botón
// @param contentColor - Color del icono y el texto
// @param onClick - Acción a ejecutar cuando el botón es presionado

@Composable
private fun DrawerMenuButton(
    text: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}