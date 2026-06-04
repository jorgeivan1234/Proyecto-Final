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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.fic.dualhabit10.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberInComposition")
@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // ---------------------------------------------------------------------------------------------
    // Variables de Registro
    // ---------------------------------------------------------------------------------------------
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha_nacimiento by remember { mutableStateOf("") }

    // Controles visuales (mostrar contraseñas, errores, calendarios, etc.)
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf("") }
    var Muestra_fecha by remember { mutableStateOf(false) }
    var camposVaciosError by remember { mutableStateOf(false) }

    // Herramienta para movernos de un campo de texto a otro al presionar "Siguiente" en el teclado
    val focusManager = LocalFocusManager.current
    val telefonoFocus = FocusRequester()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5EDCFF))
    ) {
        // Imagen de fondo que cubre toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.bg_fondo_login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Tarjeta central donde van todos los campos de texto
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(360.dp)
                .background(
                    color = Color(0xFF58B1C2),
                    shape = RoundedCornerShape(48.dp)
                )
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.Register),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = stringResource(id = R.string.Text_Register),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo: NOMBRE
            // Filtro: Solo acepta letras y espacios, máximo 40 caracteres
            TextField(
                value = nombre,
                onValueChange = { newValue ->
                    val filtrado = newValue.filter { it.isLetter() || it.isWhitespace() }
                    if (filtrado.length <= 40) {
                        nombre = filtrado
                        errorMensaje = ""
                        camposVaciosError = false
                    }
                },
                label = { Text(stringResource(R.string.label_nombre_reg)) },
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo: APELLIDO
            // Filtro: Solo acepta letras y espacios, máximo 40 caracteres
            TextField(
                value = apellido,
                onValueChange = { newValue ->
                    val filtrado = newValue.filter { it.isLetter() || it.isWhitespace() }
                    if (filtrado.length <= 40) {
                        apellido = filtrado
                        errorMensaje = ""
                        camposVaciosError = false
                    }
                },
                label = { Text(stringResource(R.string.label_apellido_reg)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo: CORREO ELECTRÓNICO
            // Filtro: Límite estricto de 30 caracteres
            TextField(
                value = email,
                onValueChange = { newValue ->
                    if (newValue.length <= 30) {
                        email = newValue
                        errorMensaje = ""
                        camposVaciosError = false
                    }
                },
                label = { Text(stringResource(R.string.label_correo_reg)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo: CONTRASEÑA
            // Filtro: Límite estricto de 12 caracteres
            TextField(
                value = password,
                onValueChange = { newValue ->
                    if (newValue.length <= 12) {
                        password = newValue
                        errorMensaje = ""
                        camposVaciosError = false
                    }
                },
                label = { Text(stringResource(R.string.label_password_reg)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                // Oculta o muestra la contraseña dependiendo del estado del ojito
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo: TELÉFONO
            // Filtro: Solo números, límite estricto de 9 caracteres
            TextField(
                value = telefono,
                onValueChange = { newValue ->
                    val filtrado = newValue.filter { it.isDigit() }
                    if (filtrado.length <= 9) {
                        telefono = filtrado
                        errorMensaje = ""
                        camposVaciosError = false
                    }
                },
                label = { Text(stringResource(R.string.label_telefono_reg)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(telefonoFocus),
                singleLine = true,
                isError = camposVaciosError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo: FECHA DE NACIMIENTO
            // Este campo está bloqueado para escritura directa; al hacer clic, abre un calendario
            TextField(
                value = fecha_nacimiento,
                onValueChange = {
                    fecha_nacimiento = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(stringResource(R.string.label_fecha_nac_reg)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { Muestra_fecha = true },
                enabled = false,
                singleLine = true,
                isError = camposVaciosError,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = Color.DarkGray,
                    disabledLabelColor = Color.DarkGray,
                    disabledIndicatorColor = Color.DarkGray
                )
            )

            // Calendario emergente
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
                        }) { Text(stringResource(R.string.btn_aceptar_reg)) }
                    },
                    dismissButton = {
                        TextButton(onClick = { Muestra_fecha = false }) {
                            Text(stringResource(R.string.btn_cancelar_reg))
                        }
                    }
                ) {
                    DatePicker(state = Status_date)
                }
            }

            // Mostrar mensajes de error en rojo si algo sale mal al intentar registrar
            if (errorMensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMensaje,
                    color = Color(0xFFD32F2F),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón en forma de texto para regresar al Login si ya tiene cuenta
            Text(
                text = stringResource(R.string.text_ya_tengo_cuenta),
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Extraemos los strings de error usando la forma nativa de Compose
            val msgErrorVacio = stringResource(R.string.error_campos_vacios_reg)
            val msgErrorPass = stringResource(R.string.error_pass_corta_reg)
            val msgErrorEmail = stringResource(R.string.error_email_invalido_reg)

            // Botón principal de Registro con todas las validaciones de seguridad
            Button(
                onClick = {
                    // Validamos que ningún campo se haya quedado en blanco
                    if (nombre.isBlank() || apellido.isBlank() || email.isBlank() ||
                        password.isBlank() || telefono.isBlank() || fecha_nacimiento.isBlank()
                    ) {
                        camposVaciosError = true
                        errorMensaje = msgErrorVacio
                    } else if (password.length < 6) {
                        // Firebase exige al menos 6 caracteres
                        camposVaciosError = false
                        errorMensaje = msgErrorPass
                    } else if (!email.contains("@")) {
                        // Validación básica de estructura de correo
                        camposVaciosError = false
                        errorMensaje = msgErrorEmail
                    } else {
                        // Si todo está perfecto, se lo mandamos a Firebase
                        camposVaciosError = false
                        authViewModel.registrarUsuario(
                            nombre = nombre,
                            apellido = apellido,
                            email = email,
                            pass = password,
                            telefono = telefono,
                            fechaNac = fecha_nacimiento,
                            onExito = {
                                // Si Firebase dice que todo bien, avanzamos
                                navController.navigate("register_successful")
                            },
                            onError = { mensajeErrorFirebase ->
                                // Si falla (ej. correo ya usado), Firebase nos avisa y lo mostramos
                                errorMensaje = mensajeErrorFirebase
                            }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4376A3)
                ),
                modifier = Modifier.size(width = 200.dp, height = 50.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.Register),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}