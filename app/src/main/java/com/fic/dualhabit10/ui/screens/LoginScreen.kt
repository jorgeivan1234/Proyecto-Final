package com.fic.dualhabit10.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController,
                authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
    }
    val tieneCuentaLocal = sharedPreferences.getBoolean("has_local_account", false)

    var email by remember { mutableStateOf (sharedPreferences.getString("saved_email", "") ?: "") }
    var password by remember { mutableStateOf (sharedPreferences.getString("saved_password", "") ?: "") }
    var recordarDatos by remember { mutableStateOf(sharedPreferences.getBoolean("remember_active", false)) }
    var passwordVisible by remember { mutableStateOf (false) }
    var errorMensaje by remember { mutableStateOf ("") }
    var camposVaciosError by remember { mutableStateOf(false)}
    val focusManager = LocalFocusManager.current

    val msjCamposVacios = stringResource(id = R.string.err_campos_vacios)
    val msjCorreoInvalido = stringResource(id = R.string.err_correo_invalido)
    val msjCredencialesIncorrectas = stringResource(id = R.string.err_credenciales_incorrectas)

    val gestionarGuardadoCredenciales: () -> Unit = {
        val editor = sharedPreferences.edit()
        if(recordarDatos){
            editor.putString("saved_email", email.trim())
            editor.putString("saved_password", password.trim())
            editor.putBoolean("remember_active", true)
            editor.putBoolean("is_logged_in", true)
        } else {
            editor.clear()
            editor.putBoolean("has_local_account", true)
        }
        editor.apply()
    }

    val ejecutarLogin= {
        val emailLimpio = email.trim()
        val passwordLimpio = password.trim()

        if (email.isBlank() || password.isBlank()){
            camposVaciosError = true
            errorMensaje = msjCamposVacios
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches()) {
            camposVaciosError = true
            errorMensaje = msjCorreoInvalido
        } else {
            camposVaciosError = false
            authViewModel.iniciarSesion(
                email = emailLimpio,
                pass = passwordLimpio,
                onExito = {
                    gestionarGuardadoCredenciales() //guardara el checkbox si esta activo
                    //si todo sale bien pasa a hábitos limpiando el historial
                    navController.navigate("habitos"){
                        popUpTo("login") { inclusive = true }
                    }
                },            //es la validación local antes de ir a internet
                onError = { mensajeErrorFirebase ->
                    //si el internet falla o da error, verifica localmente
                    val localEmail = sharedPreferences.getString("saved_email", "") ?: ""
                    val localPassword = sharedPreferences.getString("saved_password", "") ?: ""
                    val localRemember = sharedPreferences.getBoolean("remember_active", false)
                    if (localRemember && emailLimpio == localEmail && passwordLimpio == localPassword){
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("is_logged_in", true)
                        editor.apply()

                        navController.navigate("habitos"){
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        //si esta mal o el usuario no existe firebase avisara
                        errorMensaje = msjCredencialesIncorrectas
                    }
                }
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.azul_cielo))
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
                    color = colorResource(id = R.color.azul),
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
                onValueChange = {
                    email = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(text = stringResource(id = R.string.hint_correo)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
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
                onValueChange = {
                    password = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(text = stringResource(id = R.string.hint_password))},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, //que solo sea una línea y evitar saltos de línea al dar enter
                isError = camposVaciosError, //se pone rojo si está vacío
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
                            contentDescription = if (passwordVisible) stringResource(id = R.string.desc_ocultar_pass) else stringResource(id = R.string.desc_mostrar_pass)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = recordarDatos,
                    onCheckedChange = { recordarDatos = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(id = R.color.azul_fuerte),
                        uncheckedColor = Color.Black
                    )
                )
                Text(
                    text = stringResource(id = R.string.chk_recordar),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            if (errorMensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMensaje,
                    color = colorResource(id = R.color.rojo),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(id = R.string.btn_recuperar_pass),
                color = Color.White,
                fontSize =  14.sp,
                modifier = Modifier.clickable{
                    navController.navigate("forget_password")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = stringResource(id = R.string.Register),
                color = Color.White,
                fontSize =  14.sp,
                modifier = Modifier.clickable{
                    navController.navigate("register")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            //boton principal iniciar sesion
            Button(
                onClick = {
                    ejecutarLogin()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.azul_fuerte)
                ),
                modifier = Modifier.size(width = 200.dp, height = 50.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(text = stringResource(id = R.string.login),
                    color = Color.White,
                    fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (tieneCuentaLocal){
                        navController.navigate("habitos"){
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                enabled = tieneCuentaLocal,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.verde),
                    disabledContainerColor = Color.Gray
                ),
                modifier = Modifier.size(width = 200.dp, height = 50.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = if (tieneCuentaLocal) "Modo sin conexion" else "Modo offline bloqueado",
                    color = if (tieneCuentaLocal) Color.Black else Color.LightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
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