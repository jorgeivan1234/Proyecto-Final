package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch

data class SeccionHabitoMascota(
    val titulo: String,
    val rutaNavigation: String,
    val colorFondo: Color,
    val imagenRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitosMascotaScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val listaSecciones = listOf(
        SeccionHabitoMascota(
            titulo = "Hidratacion Mascota",
            rutaNavigation = "hidratacion_mascota",
            colorFondo = Color(0xFFBBDEFB),
            imagenRes = R.drawable.img_bol_y_perro
        ),
        SeccionHabitoMascota(
            titulo = "Alimentacion y Dieta",
            rutaNavigation = "alimentacion_mascota",
            colorFondo = Color(0xFFFFF0B2),
            imagenRes = R.drawable.img_plato_perro
        ),
        SeccionHabitoMascota(
            titulo = "Registro de Paseos",
            rutaNavigation = "paseos_mascota",
            colorFondo = Color(0xFFC8E6C9),
            imagenRes = R.drawable.img_perro_saltando
        ),
        SeccionHabitoMascota(
            titulo = "Higiene y Limipieza",
            rutaNavigation = "higiene_mascota",
            colorFondo = Color(0xFFE1BEE7),
            imagenRes = R.drawable.img_perro_rascandose
        ),
        SeccionHabitoMascota(
            titulo = "Salud y Vacunas",
            rutaNavigation = "salud_mascota",
            colorFondo = Color(0xFFF8BBD0),
            imagenRes = R.drawable.img_inyeccion
        ),
        SeccionHabitoMascota(
            titulo = "Actividades y Juegos",
            rutaNavigation = "juegos_mascota",
            colorFondo = Color(0xFFFFF9C4),
            imagenRes = R.drawable.img_perro_jugando
        )
    )

    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        color = Color(0xFFFF7A22),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(horizontal = 12.dp)
                    .padding(top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir Menu",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 24.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Habitos Mascotas",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
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
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(listaSecciones) { seccion ->
                        TarjetaCategoriaMascota(
                            seccion = seccion,
                            onClick = {
                                if (seccion.rutaNavigation.isNotEmpty()) {
                                    navController.navigate(seccion.rutaNavigation)
                                }
                            }
                        )
                    }
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
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = seccion.colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = seccion.imagenRes),
                contentDescription = seccion.titulo,
                modifier = Modifier.size(65.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = seccion.titulo,
                color = Color. Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
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