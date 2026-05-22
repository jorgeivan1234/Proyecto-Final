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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R



@Composable
fun LoginScreen(navController: NavHostController,
                authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf ("") }
    var password by remember { mutableStateOf ("") }
    var passwordVisible by remember { mutableStateOf (false) }
    var errorMensaje by remember { mutableStateOf ("") }
    var camposVaciosError by remember { mutableStateOf(false)}
    val focusManager = LocalFocusManager.current

    val ejecutarLogin= {
        val emailLimpio = email.trim()
        val passwordLimpio = password.trim()

        if (email.isBlank() || password.isBlank()){
            camposVaciosError = true
            errorMensaje = "Obligatorio Tiene que ingresar cuenta y contraseña"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            camposVaciosError = true
            errorMensaje = "Por favor, ingresa un correo electronico valido"
        } else {
            camposVaciosError = false
            authViewModel.iniciarSesion(
                email = emailLimpio,
                pass = passwordLimpio,
                onExito = {
                    //si todo sale bien pasa a habitos limpiando el historial
                    navController.navigate("habitos"){
                        popUpTo("login") { inclusive = true }
                    }
                },            //es la validacion local antes de ir a internet
                onError = { mensajeErrorFirebase ->
                    //si esta mal o el usuario no existe firebase avisara
                    errorMensaje = "El correo electronico o la contraseña son incorrectos"
                }
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5EDCFF))
    ){
        Image(
            painter = painterResource(id = R.drawable.bg_fondo_login),
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
            Text(
                text = stringResource(id = R.string.login),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                value = email,
                onValueChange = {email = it; errorMensaje = ""; camposVaciosError = false},
                label = { Text("Ingresar correo electronico") },
                modifier = Modifier.fillMaxWidth(),
                isError = camposVaciosError, //se pone rojo si esta vacio
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down)}
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it; errorMensaje = ""; camposVaciosError = false },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = camposVaciosError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        ejecutarLogin()
                    }
                ),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            if (errorMensaje.isNotEmpty()){
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

            Text(text = "Recuperar Contraseña",
                color = Color.White,
                fontSize =  14.sp,
                modifier = Modifier.clickable{
                    navController.navigate("forget_password")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Registrarse",
                color = Color.White,
                fontSize =  14.sp,
                modifier = Modifier.clickable{
                    navController.navigate("register")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    //es la validacion local antes de ir a internet
                    if(email.isBlank() || password.isBlank()) {
                        errorMensaje = "Por favor, llena todos los campos"
                    }else {
                        //llama ala base de datos
                        authViewModel.iniciarSesion(
                            email = email,
                            pass = password,
                            onExito = {
                                //si todo sale bien pasa a habitos limpiando el historial
                                navController.navigate("habitos"){
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onError = { mensajeErrorFirebase ->
                                //si esta mal o el usuario no existe firebase avisara
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
                Text(text = stringResource(id = R.string.login),
                    color = Color.White,
                    fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    val nav = rememberNavController()
    LoginScreen(navController = nav)
}