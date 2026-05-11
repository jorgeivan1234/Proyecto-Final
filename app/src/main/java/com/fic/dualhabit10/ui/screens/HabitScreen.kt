package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fic.dualhabit10.R

// --- COLORES PERSONALIZADOS ---
val FondoTurquesa = Color(0xFF80F3EC)
val NaranjaHeader = Color(0xFFFF8C32)
val AmarilloRacha = Color(0xFFFFFF00)
val GrisCard = Color(0xFFD9D9D9)

@Composable
fun HabitosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoTurquesa)
    ) {
        // 1. SECCIÓN SUPERIOR (Header Naranja)
        HeaderSection()

        // 2. CUADRÍCULA DE HÁBITOS
        CardsGridSection()
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp))
            .background(NaranjaHeader)
            .padding(top = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icono de Menú
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(40.dp),
                tint = Color.Black
            )
        }

        // Botón Racha Diaria
        Surface(
            shape = RoundedCornerShape(25.dp),
            color = AmarilloRacha,
            shadowElevation = 4.dp
        ) {
            Text(
                text = "Racha Diaria",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Fila de Calendario
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DayItem("Lun", "1", isActive = false)
            DayItem("Mar", "2", isActive = true)
            DayItem("Mié", "3", isActive = false)
        }
    }
}

@Composable
fun DayItem(dia: String, numero: String, isActive: Boolean) {
    val containerColor = if (isActive) AmarilloRacha else Color.Transparent
    val textColor = Color.White

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(containerColor)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = dia, fontSize = 28.sp, fontWeight = FontWeight.Black, color = textColor)
        Text(text = numero, fontSize = 28.sp, fontWeight = FontWeight.Black, color = textColor)
    }
}

@Composable
fun CardsGridSection() {
    val habitos = listOf(
        HabitoData("Hidratación", R.drawable.img_botella),
        HabitoData("Sueño", R.drawable.img_letras_zzz),
        HabitoData("Actividad física", R.drawable.img_fuerza),
        HabitoData("Alimentación", R.drawable.img_tacos),
        HabitoData("mascotas", R.drawable.img_can),
        HabitoData("Resumen", R.drawable.img_resume)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(habitos) { habito ->
            HabitoCard(habito)
        }
    }
}

@Composable
fun HabitoCard(habito: HabitoData) {
    Card(
        modifier = Modifier.fillMaxWidth().height(170.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = GrisCard)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = habito.nombre,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = habito.imagen),
                contentDescription = habito.nombre,
                modifier = Modifier.size(90.dp)
            )
        }
    }
}

data class HabitoData(val nombre: String, val imagen: Int)

@Preview(showBackground = true)
@Composable
fun PreviewHabitos() {
    HabitosScreen()
}