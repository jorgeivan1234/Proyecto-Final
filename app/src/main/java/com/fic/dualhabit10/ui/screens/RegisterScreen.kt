package com.fic.dualhabit10.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulCielo
import com.fic.dualhabit10.ui.theme.AzulBase
import com.fic.dualhabit10.ui.theme.AzulFuerte
import com.fic.dualhabit10.ui.theme.RojoError
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco

// Pantalla para la captura de datos de identidad de nuevos usuarios en el sistema local y la nube
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberInComposition")
@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha_nacimiento by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf("") }
    var Muestra_fecha by remember { mutableStateOf(false) }
    var camposVaciosError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val telefonoFocus = FocusRequester()

    // Carga de referencias de cadenas de texto localizadas para el control de validaciones
    val errCamposIncompletos = stringResource(id = R.string.err_campos_incompletos)
    val errPassCorta = stringResource(id = R.string.err_pass_corta)
    val errCorreoInvalido = stringResource(id = R.string.err_correo_invalido_format)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulCielo)
    ) {
        // Lienzo de fondo decorativo texturizado para el flujo de autenticación
        Image(
            painter = painterResource(id = R.drawable.bg_fondo_login),
            contentDescription = stringResource(id = R.string.desc_fondo_registro),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(Dimens.cardContainerWidth)
                .background(
                    color = AzulBase,
                    shape = MaterialTheme.shapes.large
                )
                .padding(Dimens.paddingLarge)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.Register),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )
            Text(
                text = stringResource(id = R.string.Text_Register),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )
            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            // Captura secuencial alfanumérica para el nombre del solicitante
            TextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(stringResource(id = R.string.hint_nombre)) },
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Captura secuencial alfanumérica para el apellido paterno/materno
            TextField(
                value = apellido,
                onValueChange = {
                    apellido = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(stringResource(id = R.string.hint_apellido)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Campo con formato restrictivo para direcciones de correo electrónico
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(stringResource(id = R.string.hint_correo)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Campo de seguridad para contraseña con máscara de caracteres conmutable
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(stringResource(id = R.string.hint_password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { telefonoFocus.requestFocus() }
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) stringResource(id = R.string.desc_ocultar_pass) else stringResource(id = R.string.desc_mostrar_pass)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Captura restrictiva de caracteres de tipo telefónico estándar
            TextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(stringResource(id = R.string.hint_telefono)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(telefonoFocus),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Campo táctil protegido que invoca el selector de calendario nativo
            TextField(
                value = fecha_nacimiento,
                onValueChange = {
                    fecha_nacimiento = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(stringResource(id = R.string.hint_fecha_nacimiento)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { Muestra_fecha = true },
                enabled = false,
                singleLine = true,
                isError = camposVaciosError,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = TextoNegro,
                    disabledLabelColor = Color.DarkGray,
                    disabledIndicatorColor = Color.DarkGray
                )
            )

            // Selector modal de fecha nativo del sistema
            if (Muestra_fecha) {
                val Status_date = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { Muestra_fecha = false },
                    confirmButton = {
                        TextButton(onClick = {
                            Status_date.selectedDateMillis?.let { millis ->
                                val Date_formate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                fecha_nacimiento = Date_formate.format(Date(millis))
                            }
                            Muestra_fecha = false
                        }) { Text(stringResource(id = R.string.btn_aceptar), color = AzulFuerte) }
                    },
                    dismissButton = {
                        TextButton(onClick = { Muestra_fecha = false }) {
                            Text(stringResource(id = R.string.btn_cancelar), color = AzulFuerte)
                        }
                    },
                    colors = androidx.compose.material3.DatePickerDefaults.colors(
                        containerColor = TextoBlanco
                    )
                ) {
                    DatePicker(state = Status_date)
                }
            }

            // Despliegue condicional de alertas de error en la validación de reglas de negocio
            if (errorMensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.paddingSmall))
                Text(
                    text = errorMensaje,
                    color = RojoError,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            // Enlace de retorno directo hacia la vista de inicio de sesión
            Text(
                text = stringResource(id = R.string.txt_iniciar_sesion_link),
                color = TextoBlanco,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Botón ejecutor que valida la integridad de los datos y procesa el alta en el ViewModel
            Button(
                onClick = {
                    if (nombre.isBlank() || apellido.isBlank() || email.isBlank() ||
                        password.isBlank() || telefono.isBlank() || fecha_nacimiento.isBlank()) {
                        camposVaciosError = true
                        errorMensaje = errCamposIncompletos
                    } else if (password.length < 8) {
                        camposVaciosError = true
                        errorMensaje = errPassCorta
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        camposVaciosError = true
                        errorMensaje = errCorreoInvalido
                    } else {
                        camposVaciosError = false
                        authViewModel.registrarUsuario(
                            nombre = nombre,
                            apellido = apellido,
                            email = email,
                            pass = password,
                            telefono = telefono,
                            fechaNac = fecha_nacimiento,
                            onExito = {
                                navController.navigate("register_successful")
                            },
                            onError = { mensajeErrorFirebase ->
                                errorMensaje = mensajeErrorFirebase
                            }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulFuerte
                ),
                modifier = Modifier.size(width = Dimens.buttonWidth, height = Dimens.buttonHeight),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = stringResource(id = R.string.Register),
                    color = TextoBlanco,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val nav = rememberNavController()
    RegisterScreen(navController = nav)
}