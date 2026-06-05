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
import androidx.compose.ui.platform.LocalFocusManager
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberInComposition")
@Composable
fun RegisterScreen(navController: NavHostController,
                   authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    /*
            Variables del Register
    */
    var nombre by remember {  mutableStateOf ("") }
    var apellido by remember { mutableStateOf ( "")}
    var email by remember { mutableStateOf ( "")}
    var password by remember { mutableStateOf ( "")}
    var telefono by remember { mutableStateOf ( "")}
    var fecha_nacimiento by remember { mutableStateOf ( "")}
    var passwordVisible by remember { mutableStateOf (false)}
    var errorMensaje by remember { mutableStateOf ( "")}
    var Muestra_fecha by remember { mutableStateOf(false)}
    var camposVaciosError by remember { mutableStateOf(false)}
    val focusManager = LocalFocusManager.current //Contraseña


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

            TextField(
                value = nombre,
                onValueChange = {nombre = it; errorMensaje = "" },
                label = { Text("Nombre (S)") },
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
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

            TextField(
                value = apellido,
                onValueChange = { apellido = it; errorMensaje = "" },
                label = { Text("Apellido (S)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
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

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMensaje = ""
                },
                label = { Text("Correo Electronico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
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

            val telefonoFocus = FocusRequester() //Contraseña
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMensaje = ""
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down)}
                ),
                visualTransformation =  if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = {passwordVisible =!passwordVisible}) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    errorMensaje = ""
                },
                label = { Text("Telefono") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(telefonoFocus),
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
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

            TextField(
                value = fecha_nacimiento,
                onValueChange = {
                    fecha_nacimiento = it
                    errorMensaje = ""
                },
                label = { Text("Fecha de nacimiento DD/MM/AAAA") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ Muestra_fecha = true },
                enabled = false,
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = Color.DarkGray,
                    disabledLabelColor = Color.DarkGray,
                    disabledIndicatorColor = Color.DarkGray
                )
            )
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
                        }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { Muestra_fecha = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = Status_date)
                }
            }

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

            Text(
                text = "Iniciar Sesion",
                color = Color.White,
                fontSize =  14.sp,
                modifier = Modifier.clickable{
                    navController.navigate("login")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || apellido.isBlank() || email.isBlank()||
                        password.isBlank() || telefono.isBlank() || fecha_nacimiento.isBlank()) {
                        errorMensaje = "Por favor, rellenar todos los campos"
                    } else if (password.length < 8) {
                        errorMensaje = "La contraseña debe tener al menos 8 caracteres"
                    } else if (!email.contains("@")) {
                        errorMensaje = "Introduce un correo electronico valido"
                    } else {
                        authViewModel.registrarUsuario(
                            nombre = nombre,
                            apellido = apellido,
                            email = email,
                            pass = password,
                            telefono = telefono,
                            fechaNac = fecha_nacimiento,
                            onExito = {
                                //si firebase dice ok avanza
                                navController.navigate("register_successful")
                            },
                            onError = { mensajeErrorFirebase ->
                                //si falla te mostrara en rojo
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
                Text(text = stringResource(id = R.string.Register),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
