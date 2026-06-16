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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.AzulFondoProgresoMascota
import com.fic.dualhabit10.ui.theme.PastelCrema
import com.fic.dualhabit10.ui.theme.PastelVerde
import com.fic.dualhabit10.ui.theme.PastelMorado
import com.fic.dualhabit10.ui.theme.PastelRosa
import com.fic.dualhabit10.ui.theme.AmarilloAlimentacion

data class SeccionHabitoMascota(
    val tituloResId: Int, // Identificador del recurso de texto para el título de la sección
    val rutaNavigation: String,
    val colorFondo: Color,
    val imagenRes: Int
)

// Pantalla principal que muestra el menú de categorías de hábitos para la mascota
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitosMascotaScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Definición de las secciones disponibles con sus respectivos estilos y rutas de navegación
    val listaSecciones = listOf(
        SeccionHabitoMascota(
            tituloResId = R.string.habito_hidratacion,
            rutaNavigation = "hidratacion_mascota",
            colorFondo = AzulFondoProgresoMascota,
            imagenRes = R.drawable.img_bol_y_perro
        ),
        SeccionHabitoMascota(
            tituloResId = R.string.habito_alimentacion,
            rutaNavigation = "alimentacion_mascota",
            colorFondo = PastelCrema,
            imagenRes = R.drawable.img_plato_perro
        ),
        SeccionHabitoMascota(
            tituloResId = R.string.habito_paseos,
            rutaNavigation = "registro_paseos",
            colorFondo = PastelVerde,
            imagenRes = R.drawable.img_perro_saltando
        ),
        SeccionHabitoMascota(
            tituloResId = R.string.habito_higiene,
            rutaNavigation = "higiene_mascota",
            colorFondo = PastelMorado,
            imagenRes = R.drawable.img_perro_rascandose
        ),
        SeccionHabitoMascota(
            tituloResId = R.string.habito_salud,
            rutaNavigation = "salud_mascota",
            colorFondo = PastelRosa,
            imagenRes = R.drawable.img_inyeccion
        ),
        SeccionHabitoMascota(
            tituloResId = R.string.habito_juegos,
            rutaNavigation = "actividad_fisica_mascota",
            colorFondo = AmarilloAlimentacion,
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
                .background(VerdeFondoHabitos)
        ) {
            // Barra superior de navegación y título de la pantalla
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.topBarHeightExtra)
                    .background(
                        color = NaranjaCabecera,
                        shape = RoundedCornerShape(
                            bottomStart = Dimens.cornerRadiusExtraLarge,
                            bottomEnd = Dimens.cornerRadiusExtraLarge
                        )
                    )
                    .padding(horizontal = Dimens.paddingMediumSmall)
                    .padding(top = Dimens.paddingLarge),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.desc_menu),
                            tint = TextoNegro,
                            modifier = Modifier.size(Dimens.iconSizeExtraLarge)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(AmarilloFondo, shape = RoundedCornerShape(Dimens.cornerRadiusLarge))
                            .padding(
                                horizontal = Dimens.paddingLarge,
                                vertical = Dimens.paddingSmallMedium
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.title_habitos_mascotas),
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro,
                            fontSize = Dimens.textSizeBodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.size(Dimens.spacerHeader))
                }
            }

            // Contenedor principal con la cuadrícula de categorías
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = Dimens.paddingDefault,
                        vertical = Dimens.paddingDefault
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.msg_selecciona_categoria),
                    fontSize = Dimens.textSizeBody,
                    fontWeight = FontWeight.Medium,
                    color = GrisOscuro,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = Dimens.paddingDefault)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.paddingDefault),
                    verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Dimens.paddingDefault)
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

// Componente visual para representar cada categoría en formato de tarjeta seleccionable
@Composable
fun TarjetaCategoriaMascota(seccion: SeccionHabitoMascota, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.tarjetaHabitoHeight)
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = seccion.colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationSmall)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = seccion.imagenRes),
                contentDescription = stringResource(seccion.tituloResId),
                modifier = Modifier.size(Dimens.iconSizeCategory),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(Dimens.paddingTiny))
            Text(
                text = stringResource(seccion.tituloResId),
                color = TextoNegro,
                fontSize = Dimens.textSizeBody,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = Dimens.lineHeightSmall
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