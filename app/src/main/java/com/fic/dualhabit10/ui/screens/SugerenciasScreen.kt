package com.fic.dualhabit10.ui.screens

import androidx.compose.material3.TextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.fic.dualhabit10.R
import androidx.navigation.NavController

@Composable
fun SugerenciasScreen(
    navController: NavController
)
{
    var sugerencias by remember { mutableStateOf("") }
    var descripcion by remember {mutableStateOf("")}
    var errorMensaje by  remember { mutableStateOf("") }
    var camposVaciosError by remember { mutableStateOf(false)}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5EDCFF))
    ){
        Image(
            painter = painterResource(id = R.drawable.bg_inicio_cielo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align (Alignment.Center)
                .width(360.dp)
                .background(
                    color = Color(0xFF58B1C2),
                    shape = RoundedCornerShape(48.dp)
                )
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Sugerencias",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold)
            Text(
                text = "Ayudanos a mejorar",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000))
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = sugerencias,
                onValueChange = {sugerencias = it; errorMensaje("") },
                label = { Text("Nombre de Sugerencia") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,))
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value ="descripcion",
                onValueChange = { descripcion = it
                    errorMensaje("")
                    camposVaciosError = false },
                label = { Text(text = "Descripción")},
                singleLine = false,
                minLines = 4, //lineas de texto minimas a 4
                maxLines = 7, //limita las lineas de texto 7
                isError = camposVaciosError,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,)
            if (errorMensaje.isNotEmpty()){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMensaje,
                    color = Color(0xFFD32F2F),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ) }
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                if (sugerencias.isBlank() || descripcion.isBlank()){
                    camposVaciosError = true
                    errorMensaje = "Por Favor de rellenar todos los campos"
                }else{()
                    errorMensaje ="Sugerencia enviada correctamente"
                    sugerencias = ""
                    descripcion = ""
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4376A3)),
                modifier = Modifier.size(width = 200.dp, height = 50.dp),
                shape = RoundedCornerShape(50.dp)
            )
            {
                Text(
                    text = "Enviar",
                    color = Color.White,
                    fontSize = 16.sp
                    )
            }
        }
    }
}