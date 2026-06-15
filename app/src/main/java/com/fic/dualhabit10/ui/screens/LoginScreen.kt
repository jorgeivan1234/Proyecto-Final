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
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AuthViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulCielo
import com.fic.dualhabit10.ui.theme.AzulBase
import com.fic.dualhabit10.ui.theme.AzulFuerte
import com.fic.dualhabit10.ui.theme.VerdeDual
import com.fic.dualhabit10.ui.theme.RojoError
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco

// Pantalla de autenticación y acceso con soporte offline
@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
    }
    val tieneCuentaLocal = sharedPreferences.getBoolean("has_local_account", false)

    var email by remember { mutableStateOf(sharedPreferences.getString("saved_email", "") ?: "") }
    var password by remember { mutableStateOf(sharedPreferences.getString("saved_password", "") ?: "") }
    var recordarDatos by remember { mutableStateOf(sharedPreferences.getBoolean("remember_active", false)) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf("") }
    var camposVaciosError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val msjCamposVacios = stringResource(id = R.string.err_campos_vacios)
    val msjCorreoInvalido = stringResource(id = R.string.err_correo_invalido)
    val msjCredencialesIncorrectas = stringResource(id = R.string.err_credenciales_incorrectas)

    val gestionarGuardadoCredenciales: () -> Unit = {
        val editor = sharedPreferences.edit()
        if (recordarDatos) {
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

    val ejecutarLogin = {
        val emailLimpio = email.trim()
        val passwordLimpio = password.trim()

        if (email.isBlank() || password.isBlank()) {
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
                    gestionarGuardadoCredenciales()
                    navController.navigate("habitos") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onError = { mensajeErrorFirebase ->
                    val localEmail = sharedPreferences.getString("saved_email", "") ?: ""
                    val localPassword = sharedPreferences.getString("saved_password", "") ?: ""
                    val localRemember = sharedPreferences.getBoolean("remember_active", false)
                    if (localRemember && emailLimpio == localEmail && passwordLimpio == localPassword) {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("is_logged_in", true)
                        editor.apply()

                        navController.navigate("habitos") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorMensaje = msjCredencialesIncorrectas
                    }
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulCielo)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_fondo_login),
            contentDescription = stringResource(id = R.string.desc_fondo_login),
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
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.headlineLarge,
                color = TextoNegro
            )
            Spacer(modifier = Modifier.height(Dimens.spacerLarge))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(text = stringResource(id = R.string.hint_correo)) },
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
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMensaje = ""
                    camposVaciosError = false
                },
                label = { Text(text = stringResource(id = R.string.hint_password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
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
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = recordarDatos,
                    onCheckedChange = { recordarDatos = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AzulFuerte,
                        uncheckedColor = TextoNegro
                    )
                )
                Text(
                    text = stringResource(id = R.string.chk_recordar),
                    color = TextoBlanco,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (errorMensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.paddingSmall))
                Text(
                    text = errorMensaje,
                    color = RojoError,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            Text(
                text = stringResource(id = R.string.btn_recuperar_pass),
                color = TextoBlanco,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navController.navigate("forget_password")
                }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingSmall))

            Text(
                text = stringResource(id = R.string.Register),
                color = TextoBlanco,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            // Botón principal para iniciar sesión
            Button(
                onClick = { ejecutarLogin() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulFuerte
                ),
                modifier = Modifier.size(
                    width = Dimens.buttonWidth,
                    height = Dimens.buttonHeight
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    color = TextoBlanco,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Botón para iniciar sesión en modo local sin conexión
            Button(
                onClick = {
                    if (tieneCuentaLocal) {
                        navController.navigate("habitos") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                enabled = tieneCuentaLocal,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeDual,
                    disabledContainerColor = Color.Gray
                ),
                modifier = Modifier.size(
                    width = Dimens.buttonWidth,
                    height = Dimens.buttonHeight
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = if (tieneCuentaLocal) stringResource(id = R.string.btn_modo_offline) else stringResource(id = R.string.btn_offline_bloqueado),
                    color = if (tieneCuentaLocal) TextoNegro else Color.LightGray,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val nav = rememberNavController()
    LoginScreen(navController = nav)
}