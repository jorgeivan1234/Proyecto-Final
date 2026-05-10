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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.fic.dualhabit10.R



@Composable
fun LoginScreen(navController: NavHostController) {

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

            var email by remember { mutableStateOf("") }
                TextField(
                    value = email,
                    onValueChange = {email = it},
                    label = { Text("Ingresar correo electronico") },
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
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                )
            )
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Agregar logica para ir a la pantalla principal */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4376A3) // Azul oscuro
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
