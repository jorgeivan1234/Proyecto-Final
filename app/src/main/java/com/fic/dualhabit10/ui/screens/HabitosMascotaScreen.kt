package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class SeccionHabitoMascota(
    val titulo: String,
    val rutaNavigation: String,
    val colorFondo: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitosMascotaScreen(navController: NavController) {
    val listaSecciones = listOf(
        SeccionHabitoMascota(
            titulo = "Hidratacion Mascota",
            rutaNavigation = "hidratacion_mascota",
            colorFondo = Color(0xFFBBDEFB)
        ),
        SeccionHabitoMascota(
            titulo = "Alimentacion y Dieta",
            rutaNavigation = "alimentacion_mascota",
            colorFondo = Color(0xFFFFF0B2)
        ),
        SeccionHabitoMascota(
            titulo = "Registro de Paseos",
            rutaNavigation = "paseos_mascota",
            colorFondo = Color(0xFFC8E6C9)
        ),
        SeccionHabitoMascota(
            titulo = "Higiene y Limipieza",
            rutaNavigation = "higiene_mascota",
            colorFondo = Color(0xFFE1BEE7)        ),
        SeccionHabitoMascota(
            titulo = "Salud y Vacunas",
            rutaNavigation = "salud_mascota",
            colorFondo = Color(0xFFF8BBD0)
        ),
        SeccionHabitoMascota(
            titulo = "Actividades y Juegos",
            rutaNavigation = "juegos_mascota",
            colorFondo = Color(0xFFFFF9C4)
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    ){
                        Text("Habitos Mascotas", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF7A22))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Selecciona una categoria para gestionar el cuidado de tu mascota",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(listaSecciones) { seccion ->
                    TarjetaCategoriaMascota(
                        seccion = seccion,
                        onClick = {
                            if (seccion.rutaNavigation.isNotEmpty()){
                                navController.navigate(seccion.rutaNavigation)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaCategoriaMascota(seccion: SeccionHabitoMascota, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = seccion.colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = seccion.titulo,
                color = Color. Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewHabitosMascotaScreen(){
    val nav = rememberNavController()
    HabitosMascotaScreen(navController = nav)
}