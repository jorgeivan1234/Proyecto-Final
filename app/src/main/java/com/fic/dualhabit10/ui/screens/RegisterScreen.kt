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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.fic.dualhabit10.R


@Composable
fun RegisterScreen(navController: NavHostController) {

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

            var nombre by remember { mutableStateOf("") }
            TextField(
                value = nombre,
                onValueChange = {nombre = it},
                label = { Text("Nombre (S)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            var apellido by remember { mutableStateOf("") }
            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido (S)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            var password by remember { mutableStateOf("") }
            TextField(
                value = password,
                onValueChange = {password = it},
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            var email by remember { mutableStateOf("") }
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electronico") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            var telefono by remember { mutableStateOf("") }
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Telefono") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            var fecha_nacimiento by remember { mutableStateOf("") }
            TextField(
                value = fecha_nacimiento,
                onValueChange = { fecha_nacimiento = it },
                label = { Text("Fecha de nacimiento DD/MM/AAAA") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Iniciar Sesion",
                color = Color.White,
                fontSize =  14.sp,
                modifier = Modifier.clickable{
                    navController.navigate("login")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("register_successful")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4376A3)
                ),
                modifier = Modifier.size(width = 200.dp, height = 50.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(text = stringResource(id = R.string.Register),
                    color = Color.White,
                    fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    RegisterScreen(navController = navController)
}
