package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulCielo
import com.fic.dualhabit10.ui.theme.AzulBase
import com.fic.dualhabit10.ui.theme.AzulFuerte
import com.fic.dualhabit10.ui.theme.RojoError
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco

// Pantalla responsable de gestionar la recuperación de contraseña del usuario
@Composable
fun Forget_Password(
    navController: NavHostController,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Declaración de estados locales para el control de la interfaz y los errores
    var email by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }
    var mostrarAlertaExito by remember { mutableStateOf(false) }
    var campoError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Carga de mensajes de error desde los recursos para facilitar su mantenimiento
    val msjCorreoVacio = stringResource(id = R.string.err_correo_vacio_recuperacion)
    val msjCorreoInvalido = stringResource(id = R.string.err_correo_invalido_recuperacion)
    val msjCorreoNoRegistrado = stringResource(id = R.string.err_correo_no_registrado)

    // Función encargada de validar y procesar la solicitud de recuperación de contraseña
    val procesarRecuperacion = {
        val emailLimpio = email.trim()
        if (emailLimpio.isBlank()) {
            campoError = true
            errorMensaje = msjCorreoVacio
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            campoError = true
            errorMensaje = msjCorreoInvalido
        } else {
            campoError = false
            authViewModel.enviarEnlaceRecuperacion(
                email = emailLimpio,
                onExito = {
                    mostrarAlertaExito = true
                },
                onError = { _ ->
                    errorMensaje = msjCorreoNoRegistrado
                }
            )
        }
    }

    // Contenedor principal con imagen de fondo ajustada a la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulCielo)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_inicio_cielo),
            contentDescription = stringResource(id = R.string.desc_fondo_cielo_recuperar),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Tarjeta central que agrupa los elementos del formulario
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(Dimens.cardContainerWidth)
                .background(
                    color = AzulBase,
                    shape = MaterialTheme.shapes.large
                )
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_perro_saltando),
                contentDescription = stringResource(id = R.string.desc_perro_saltando),
                modifier = Modifier.size(Dimens.imageDogMedium)
            )
            Text(
                text = stringResource(R.string.text_forget_password),
                style = MaterialTheme.typography.titleLarge,
                color = TextoNegro,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Campo de entrada de texto para el correo con validación de estado
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMensaje = ""
                    campoError = false
                },
                label = { Text(stringResource(id = R.string.hint_correo_electronico)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = campoError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        focusManager.clearFocus()
                        procesarRecuperacion()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )

            if (errorMensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.paddingSmall))
                Text(
                    text = errorMensaje,
                    color = RojoError,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(Dimens.spacerMedium))

            // Botón de acción que ejecuta el proceso de validación y envío
            Button(
                onClick = {
                    focusManager.clearFocus()
                    procesarRecuperacion()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulFuerte
                ),
                modifier = Modifier.size(width = Dimens.buttonWidth, height = Dimens.buttonHeight),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = stringResource(id = R.string.Send_Code),
                    color = TextoBlanco,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            // Enlace interactivo para regresar a la pantalla de inicio de sesión
            Text(
                text = stringResource(id = R.string.Login_Back),
                color = TextoBlanco,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navController.navigate("login") {
                        popUpTo("forget_password") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }

    // Cuadro de diálogo para notificar el envío exitoso del correo de recuperación
    if (mostrarAlertaExito) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaExito = false },
            title = {
                Text(
                    text = stringResource(id = R.string.title_correo_enviado),
                    fontWeight = FontWeight.Bold
                )
            },
            // Se incluye el correo ingresado como parámetro dinámico en el mensaje de confirmación
            text = {
                Text(text = stringResource(id = R.string.msg_correo_enviado, email))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarAlertaExito = false
                        navController.navigate("login")
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_entendido),
                        fontWeight = FontWeight.Bold,
                        color = AzulFuerte
                    )
                }
            },
            shape = MaterialTheme.shapes.medium,
            containerColor = TextoBlanco
        )
    }
}