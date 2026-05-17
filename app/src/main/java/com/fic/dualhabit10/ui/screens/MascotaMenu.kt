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
    val enMantenimiento: Boolean = false
)

@Composable
fun MascotasMenu(navController: NavController) {

    //La lista las tarjetas
    val Listacards = listOf(
        MascotaMenu(
            titulo = "Noticias de mascotas",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | noticia_mascota | Cuando se cree la pantalla
            enMantenimiento = true
        ),
        MascotaMenu(
            titulo = "Juegos",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | juegos | Cuando se cree la pantalla
            enMantenimiento = true
        ),
        MascotaMenu(
            titulo = "Enciclopedia",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | enciclopedia | Cuando se cree la pantalla
            enMantenimiento = true
        ),
        MascotaMenu(
            titulo = "Ir a mis hábitos humanos",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | habitos_humanos | Cuando se cree la pantalla
            enMantenimiento = true
        ),
        MascotaMenu(
            titulo = "Aprender (PetDex)",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | petdex | Cuando se cree la pantalla
            enMantenimiento = true
        ),
        MascotaMenu(
            titulo = "Ejercicios",
            imagenRes = null,
            rutaNavigation = "", //Agregar ruta. ejmp | ejercicios | Cuando se cree la pantalla
            enMantenimiento = true
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
            onClick = { navController.navigate("mascota_home") }
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
                    onClick = { navController.navigate(item.rutaNavigation) }
                )
            }
        }
    }
}
//Funcion para la tarjeta grande de la mascota
@Composable
fun Tarjeta_Mascota_Grande(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD2D2D2))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.Ver_Mascota),
                color = Color.Black,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Image(
                painter = painterResource(id = R.drawable.img_perro_coloreado),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD2D2D2))
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            // Etiqueta "(mantenimiento)" igual que en la imagen
            if (item.enMantenimiento) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "(mantenimiento)",
                    color = Color.DarkGray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
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