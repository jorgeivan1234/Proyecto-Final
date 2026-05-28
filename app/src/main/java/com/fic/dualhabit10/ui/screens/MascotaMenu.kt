package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

data class MascotaMenu(
    val titulo: String,
    val imagenRes: Int?, //El ? Sirve para poder poner null en el imagenres en caso de que las cards no tengan imagen
    val rutaNavigation: String,
    val enMantenimiento: Boolean = false,
    val colorFondo: Color
)

@Composable
fun MascotasMenu(navController: NavController) {

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
            titulo = "Noticias de mascotas",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | noticia_mascota | Cuando se cree la pantalla
            enMantenimiento = true,
            colorFondo = colores[0]
        ),
        MascotaMenu(
            titulo = "Juegos",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | juegos | Cuando se cree la pantalla
            enMantenimiento = true,
            colorFondo = colores[1]
        ),
        MascotaMenu(
            titulo = "Enciclopedia",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | enciclopedia | Cuando se cree la pantalla
            enMantenimiento = true,
            colorFondo = colores[2]
        ),
        MascotaMenu(
            titulo = "Ir a mis hábitos humanos",
            imagenRes = null,
            rutaNavigation = "habitos",
            enMantenimiento = false,
            colorFondo = colores[3]
        ),
        MascotaMenu(
            titulo = "Aprender (PetDex)",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | petdex | Cuando se cree la pantalla
            enMantenimiento = true,
            colorFondo = colores[4]
        ),
        MascotaMenu(
            titulo = "Ejercicios",
            imagenRes = null,
            rutaNavigation = "actividad_fisica_mascota",
            enMantenimiento = false,
            colorFondo = colores[5]
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9EFFEB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF7A22))
                .padding(start = 12.dp, end = 12.dp, bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // Botón regresar (flecha atrás)
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
                // Título centrado
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(id = R.string.Menu_Mascota),
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(Listacards) { item ->
                TarjetaMenuMascota(
                    item = item,
                    onClick = {
                        if (!item.enMantenimiento && item.rutaNavigation.isNotEmpty()){
                            navController.navigate(item.rutaNavigation)
                        }
                    }
                )
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

// -------------------------------------------------------
// Preview
// -------------------------------------------------------
@Preview
@Composable
fun PreviewMascotasMenu() {
    val nav = rememberNavController()
    MascotasMenu(navController = nav)
}