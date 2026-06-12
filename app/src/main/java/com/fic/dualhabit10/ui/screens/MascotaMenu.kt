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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch

data class MascotaMenu(
    val titulo: String,
    val imagenRes: Int?, //El ? Sirve para poder poner null en el imagenres en caso de que las cards no tengan imagen
    val rutaNavigation: String,
    val enMantenimiento: Boolean = false,
    val colorFondo: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasMenu(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val colores = listOf(
        Color(0xFFFFE0B2),
        Color(0xFFE1BEE7),
        Color(0xFFC8E6C9),
        Color(0xFFFFF9C4),
        Color(0xFFF8BBD0),
        Color(0xFFBBDEFB)
    )
    //La lista las tarjetas
    val Listacards = listOf(
        MascotaMenu(
            titulo = "Ejercicios",
            imagenRes = null,
            rutaNavigation = "actividad_fisica_mascota",
            enMantenimiento = false,
            colorFondo = colores[0]
        ),

        MascotaMenu(
            titulo = "Ir a mis hábitos humanos",
            imagenRes = null,
            rutaNavigation = "habitos",
            enMantenimiento = false,
            colorFondo = colores[1]
        ),
        MascotaMenu(
            titulo = "Noticias de mascotas",
            imagenRes = null,
            rutaNavigation = "mantenimiento", //Redirige a mantenimiento hasta proxima actualizacion.
            enMantenimiento = true,
            colorFondo = colores[2]
        ),
        MascotaMenu(
            titulo = "Juegos",
            imagenRes = null,
            rutaNavigation = "mantenimiento", //Redirige a mantenimiento hasta proxima actualizacion.
            enMantenimiento = true,
            colorFondo = colores[3]
        ),
        MascotaMenu(
            titulo = "Enciclopedia",
            imagenRes = null,
            rutaNavigation = "mantenimiento", //Redirige a mantenimiento hasta proxima actualizacion.
            enMantenimiento = true,
            colorFondo = colores[4]
        ),

        MascotaMenu(
            titulo = "Aprender (PetDex)",
            imagenRes = null,
            rutaNavigation = "mantenimiento", //Redirige a mantenimiento hasta proxima actualizacion.
            enMantenimiento = true,
            colorFondo = colores[5]
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
                        color = Color (0xFFFF7A22),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(horizontal  = 12.dp)
                    .padding(top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //menu hamburgesa
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir Menu Lateral",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    // Título amarillo
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.Menu_Mascota),
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(48.dp))

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Tarjeta_Mascota_Grande(
                colorFondo = Color(0xFFF5F5F5),
                onClick = { navController.navigate("habitos_mascota_menu") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Esto es la separacion de las cards en 2 filas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(Listacards) { item ->
                    TarjetaMenuMascota(
                        item = item,
                        onClick = {
                            if (item.rutaNavigation.isNotEmpty()){
                                navController.navigate(item.rutaNavigation)
                            }
                        }
                    )
                }
            }
        }
    }
}
//Funcion para la tarjeta grande de la mascota
@Composable
fun Tarjeta_Mascota_Grande(colorFondo: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Habitos de mascota",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1.2f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(id = R.drawable.img_perro_coloreado),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

//Funcion de las tarjetas abajo de la mascota virtual
@Composable
fun TarjetaMenuMascota(item: MascotaMenu, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = item.colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.titulo,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            // Etiqueta "(mantenimiento)" igual que en la imagen
            if (item.enMantenimiento) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "(mantenimiento)",
                    color = Color.DarkGray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMascotasMenu() {
    val nav = rememberNavController()
    MascotasMenu(navController = nav)
}