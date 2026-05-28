package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fic.dualhabit10.R



@Composable
fun InicioScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.azul_cielo))
    ) {
        //imagen del fondo
        Image(
            painter = painterResource(id = R.drawable.bg_inicio_cielo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        //titulo
        Text(
            text = stringResource(R.string.bienvenido_a_dualhabit),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 60.dp)
        )
        // boton de iniciar sesion
        Button(
            onClick = {
                navController.navigate("login")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.verde)
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 140.dp)
                .size(width = 200.dp, height = 65.dp)
        ) {
            Text(
                text = stringResource(R.string.iniciar),
                color = Color.Black,
                fontSize = 18.sp
            )
        }
        //personaje
        Image(
            painter = painterResource(
                id = R.drawable.img_personaje_saludo
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 70.dp)
                .size(width = 312.dp, height = 559.dp)
        )
        //perro
        Image(
            painter = painterResource(
                id = R.drawable.img_perro_inicio
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-96).dp, y = 50.dp)
                .scale(scaleX = -1f, scaleY = 1f)
                .size(306.dp)
        )
        //bienvenida
        Image(
            painter = painterResource(
                id = R.drawable.img_globo_welcome
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 90.dp, y = (-260).dp)
        )
    }
}