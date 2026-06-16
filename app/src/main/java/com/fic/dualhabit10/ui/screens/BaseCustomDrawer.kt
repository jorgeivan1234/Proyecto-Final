package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AuthUiState
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.RojoError
import com.fic.dualhabit10.ui.theme.RojoOscuroPeligro
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.GrisSeparador

// Constantes de las pantallas
private object AppRoutes {
    const val INICIO = "inicio"
    const val HABITOS = "habitos"
    const val PERFIL = "perfil"
    const val LOGIN = "login"
}

// Nombres clave que nos ayudan para mantener iniciada la sesión del usuario y no repita el loggin cada vez
private object AuthPrefs {
    const val FILE_NAME = "login_preferences"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
}

// Modelado de menú lateral
@Composable
fun BaseCustomDrawer(
    navController: NavController,
    drawerState: DrawerState,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    content: @Composable () -> Unit
) {
    // Herramientas que nos ayudan a abrir pantallas y realizar acciones de fondo
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    val uiState by authViewModel.uiState.collectAsState()

    // Creamos el cajón del menú lateral
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = VerdeFondoHabitos,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Dimens.drawerWidth)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimens.spacerMedium),
                    horizontalAlignment = Alignment.Start
                ) {

                    // Nombre de la app en grande con línea divisora
                    Text(
                        text = stringResource(R.string.app_title),
                        color = NaranjaCabecera,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = Dimens.paddingDefault)
                    )
                    HorizontalDivider(
                        color = GrisSeparador,
                        thickness = Dimens.dividerThickness
                    )
                    Spacer(modifier = Modifier.height(Dimens.spacerMedium))

                    // Botón para ir a la pantalla principal donde están los hábitos
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_home),
                        icon = Icons.Default.Home,
                        containerColor = NaranjaCabecera,
                        contentColor = TextoNegro
                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppRoutes.HABITOS) {
                            popUpTo(AppRoutes.HABITOS) { inclusive = false }
                            launchSingleTop = true
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                    // Botón para que el usuario pueda ver o cambiar su información
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_profile),
                        icon = Icons.Default.AccountCircle,
                        containerColor = NaranjaCabecera,
                        contentColor = TextoNegro
                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppRoutes.PERFIL)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón de color rojo oscuro para borrar la cuenta si el usuario ya no quiere utilizar la app
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_delete_account),
                        icon = Icons.Default.Delete,
                        containerColor = RojoOscuroPeligro,
                        contentColor = TextoBlanco
                    ) {
                        showDeleteDialog = true
                    }

                    Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                    // Botón para salir de la cuenta y regresar a la pantalla de inicio
                    DrawerMenuButton(
                        text = stringResource(R.string.menu_logout),
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        containerColor = RojoError,
                        contentColor = TextoBlanco
                    ) {
                        scope.launch { drawerState.close() }

                        // Borramos el recuerdo de que el usuario había entrado para que pida la contraseña la próxima vez
                        val sharedPreferences = context.getSharedPreferences(AuthPrefs.FILE_NAME, Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean(AuthPrefs.KEY_IS_LOGGED_IN, false).apply()

                        authViewModel.resetUiState()

                        navController.navigate(AppRoutes.INICIO) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        }
    ) {

        // Aquí dibujamos el contenido de la pantalla que esté abierta en ese momento
        content()

        // Ventana flotante con texto de advertencia sobre borrar la cuenta
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(R.string.dialog_delete_title)) },
                text = { Text(stringResource(R.string.dialog_delete_body)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false

                            // Si el usuario acepta borramos todos sus datos y lo mandamos a la pantalla de bienvenida
                            authViewModel.eliminarCuenta(onSuccess = {
                                scope.launch { drawerState.close() }
                                val sharedPreferences = context.getSharedPreferences(AuthPrefs.FILE_NAME, Context.MODE_PRIVATE)
                                sharedPreferences.edit().clear().apply()

                                navController.navigate(AppRoutes.INICIO) {
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
                        Text(stringResource(R.string.dialog_btn_cancel), color = TextoNegro)
                    }
                }
            )
        }

        // Aquí revisamos qué está pasando con la cuenta para mostrar distintos mensajes
        when (uiState) {

            // Circulo de acción en proceso
            is AuthUiState.Loading -> {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {},
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(color = NaranjaCabecera)
                            Spacer(modifier = Modifier.width(Dimens.paddingDefault))
                            Text(stringResource(R.string.dialog_loading))
                        }
                    }
                )
            }

            // Si necesitamos confirmar que es el dueño de la cuenta le pedimos que inicie sesión de nuevo
            is AuthUiState.RequiresReauth -> {
                AlertDialog(
                    onDismissRequest = { authViewModel.resetUiState() },
                    title = { Text(stringResource(R.string.dialog_reauth_title)) },
                    text = { Text(stringResource(R.string.dialog_reauth_body)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                authViewModel.resetUiState()
                                scope.launch { drawerState.close() }
                                navController.navigate(AppRoutes.INICIO) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        ) {
                            Text(stringResource(R.string.dialog_btn_goto_login))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { authViewModel.resetUiState() }) {
                            Text(stringResource(R.string.dialog_btn_cancel), color = TextoNegro)
                        }
                    }
                )
            }

            // Mensaje de error
            is AuthUiState.Error -> {
                val errorMsg = (uiState as AuthUiState.Error).theMessage
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                authViewModel.resetUiState()
            }
            else -> {}
        }
    }
}

// Diseño de botón reutilizable
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
        shape = RoundedCornerShape(Dimens.paddingMedium)
    ) {

        // Posicionamiento de iconos y textos
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.paddingTiny)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(Dimens.iconSizeMedium)
            )
            Spacer(modifier = Modifier.width(Dimens.paddingMedium))
            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}