package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import com.fic.dualhabit10.R



data class HabitoItem(
    val titulo: String,
    val imagenRes: Int,
    val rutaNavigation: String
)

@Composable
fun HabitosScreen(navController: NavController) {
    val listaHabitos = listOf(
        HabitoItem("Hidratacion", R.drawable.img_hidratacion, "hidratacion_screen"),
        HabitoItem("Sueño", R.drawable.img_sueno, "sueno_screen"),
        HabitoItem("Actividad\nFisica", R.drawable.img_ejercicio, "ejercicio_screen"),
        HabitoItem("Alimentacion", R.drawable.img_alimentacion, "alimentacion_screen"),
        HabitoItem("mascotas", R.drawable.img_mascotas_v, "mascotas_screen"),
        HabitoItem("Resumen", R.drawable.img_resumen, "resumen_screen")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9EFFEB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFFF7A22),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .statusBarsPadding()
                .padding(start = 12.dp, end = 12.dp, bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "Racha Diaria",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DiaItem(diaSemana = "Lun", numeroDia = "1", esSeleccionado = true)
                DiaItem(diaSemana = "Mar", numeroDia = "2", esSeleccionado = false)
                DiaItem(diaSemana = "Mar", numeroDia = "3", esSeleccionado = false)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), //para que sean nomas dos columnas
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal =  16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(listaHabitos) { habito ->
                Tarjetahabito(habito = habito, onClick = {
                    navController.navigate(habito.rutaNavigation)
                })
            }
        }
    }
}

@Composable
fun DiaItem(diaSemana: String, numeroDia: String, esSeleccionado: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = if (esSeleccionado) Color (0xFFFFF200) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = diaSemana,
            color = if (esSeleccionado) Color.Black else Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text (
            text = numeroDia,
            color = if (esSeleccionado) Color.Black else Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Tarjetahabito(habito: HabitoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable{ onClick ()},
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD2D2D2)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = habito.titulo,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Image(
                painter = painterResource(id = habito.imagenRes),
                contentDescription = habito.titulo,
                modifier = Modifier
                    .size(85.dp)
                    .padding(bottom = 4.dp)
            )
        }
    }
}