package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.fic.dualhabit10.R

@Composable
fun RegisterSuccessful(navController: NavHostController) {

    // Contenedor principal que ocupa toda la pantalla con un fondo azul sólido de respaldo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5EDCFF))
    ) {
        // Imagen de fondo (cielo/nubes) que decora la pantalla completa
        Image(
            painter = painterResource(id = R.drawable.bg_inicio_cielo),
            contentDescription = stringResource(R.string.desc_fondo_cielo),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Tarjeta central flotante donde mostramos el mensaje de éxito y al perro
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
            // Título principal
            Text(
                text = stringResource(id = R.string.title_registro_exitoso),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(55.dp))

            // Imagen ilustrativa
            Image(
                painter = painterResource(id = R.drawable.img_perro_saltando),
                contentDescription = stringResource(R.string.desc_perro_saltando),
                modifier = Modifier.size(280.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón final para avanzar a la pantalla de inicio de sesión (Login)
            Button(
                onClick = {
                    // Una vez que el usuario lee el mensaje, lo mandamos al login
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4376A3)
                ),
                modifier = Modifier.size(width = 200.dp, height = 50.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.btn_continuar),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
