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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import com.fic.dualhabit10.R



@Composable
fun Forget_Password(navController: NavHostController,
                    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf("")}
    var errorMensaje by remember { mutableStateOf("")}
    var mostrarAlertaExito by remember { mutableStateOf(false)}
    var campoError by remember {mutableStateOf(false)}
    val focusManager = LocalFocusManager.current
    val procesarRecuperacion = {
        val emailLimpio = email.trim()
        if (emailLimpio.isBlank()){
            campoError = true
            errorMensaje = "Obligatorio: Por favor ingrese su cuenta de correo"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            campoError = true
            errorMensaje = "Por favor, ingresa un formato de correo valido"
        } else {
            campoError = false
            authViewModel.enviarEnlaceRecuperacion(
                email = emailLimpio,
                onExito = {
                    mostrarAlertaExito = true
                },
                onError = { mensajeError ->
                    errorMensaje = "No encontramos nunguna cuenta registrada con este correo"
                }
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5EDCFF))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_inicio_cielo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(360.dp)
                .background(
                    color = Color(0xFF58B1C2),
                    shape = RoundedCornerShape(48.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_perro_saltando),
                contentDescription = null,
                modifier = Modifier.size(240.dp)
            )
            Text(
                text = stringResource(R.string.text_forget_password),
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = email,
                onValueChange = {email = it; errorMensaje = ""; campoError = false },
                label = { Text("Correo electronico") },
                modifier = Modifier.fillMaxWidth(),
                isError = campoError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Send  // el boton enter cambia a icono de enviar
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

            if (errorMensaje.isNotEmpty()){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMensaje,
                    color = Color(0xFFD32F2F),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (email.isBlank()) {
                        errorMensaje = "El campo no puede estar vacio"
                    } else if (!email.contains("@")) {
                        errorMensaje = "Formato de correo invalido"
                    } else {
                        authViewModel.enviarEnlaceRecuperacion(
                            email = email,
                            onExito = {
                                mostrarAlertaExito = true
                            },
                            onError = { mensajeError ->
                                errorMensaje = mensajeError
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
                Text(text = stringResource(id = R.string.Send_Code),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.Login_Back),
                color = Color.White,
                fontSize =  14.sp,
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

    if (mostrarAlertaExito) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaExito = false},
            title = {Text(text = "Correo Enviado", fontWeight = FontWeight.Bold)},
            text = {Text(text = "Hemos enviado un enlace de recuperacion a:\n$email\n\nRevisa tu bandeja de entrada o spam")},
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarAlertaExito = false
                        navController.navigate("login")
                    }
                ) {
                    Text(text = "Entendido",  fontWeight = FontWeight.Bold, color = Color(0xFF4376A3))
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}
