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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AmarilloAlimentacion
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.AzulFondoProgresoMascota
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.FondoTarjetaBlanco
import com.fic.dualhabit10.ui.theme.NaranjaFondoClima
import com.fic.dualhabit10.ui.theme.PastelMorado
import com.fic.dualhabit10.ui.theme.PastelVerde
import com.fic.dualhabit10.ui.theme.PastelRosa

// Entidad que representa cada una de las opciones disponibles en el panel de la mascota virtual
data class MascotaMenu(
    val tituloResId: Int,
    val imagenRes: Int?,
    val rutaNavigation: String,
    val enMantenimiento: Boolean = false,
    val colorFondo: Color
)

// Pantalla principal del menú de la mascota virtual con accesos a juegos, hábitos y enciclopedia
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasMenu(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val Listacards = listOf(
        MascotaMenu(
            tituloResId = R.string.menu_ejercicios,
            imagenRes = null,
            rutaNavigation = "actividad_fisica_mascota",
            enMantenimiento = false,
            colorFondo = NaranjaFondoClima
        ),
        MascotaMenu(
            tituloResId = R.string.menu_ir_habitos_humanos,
            imagenRes = null,
            rutaNavigation = "habitos",
            enMantenimiento = false,
            colorFondo = PastelMorado
        ),
        MascotaMenu(
            tituloResId = R.string.menu_noticias,
            imagenRes = null,
            rutaNavigation = "mantenimiento",
            enMantenimiento = true,
            colorFondo = PastelVerde
        ),
        MascotaMenu(
            tituloResId = R.string.menu_juegos,
            imagenRes = null,
            rutaNavigation = "mantenimiento",
            enMantenimiento = true,
            colorFondo = AmarilloAlimentacion
        ),
        MascotaMenu(
            tituloResId = R.string.menu_enciclopedia,
            imagenRes = null,
            rutaNavigation = "mantenimiento",
            enMantenimiento = true,
            colorFondo = PastelRosa
        ),
        MascotaMenu(
            tituloResId = R.string.menu_aprender,
            imagenRes = null,
            rutaNavigation = "mantenimiento",
            enMantenimiento = true,
            colorFondo = AzulFondoProgresoMascota
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
            // Contenedor superior para la barra de navegación personalizada
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
                    .padding(horizontal = Dimens.paddingMedium)
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
                                horizontal = Dimens.paddingMedium,
                                vertical = Dimens.paddingSmallMedium
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.Menu_Mascota),
                            color = TextoNegro,
                            fontSize = Dimens.textSizeBodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Margen de compensación para equilibrar la alineación del título
                    Spacer(modifier = Modifier.size(Dimens.spacerHeader))
                }
            }

            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            Tarjeta_Mascota_Grande(
                colorFondo = FondoTarjetaBlanco,
                onClick = { navController.navigate("habitos_mascota_menu") }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            // Rejilla de dos columnas con las secciones secundarias del menú
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingDefault),
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault),
                contentPadding = PaddingValues(bottom = Dimens.paddingLarge)
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

// Tarjeta destacada de dimensiones mayores para acceder a los hábitos principales de la mascota
@Composable
fun Tarjeta_Mascota_Grande(colorFondo: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.cardMenuGrandeHeight)
            .padding(horizontal = Dimens.paddingDefault)
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingDefault),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.menu_habitos_mascota),
                color = TextoNegro,
                fontSize = Dimens.textSizeSubtitle,
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
                    .padding(Dimens.paddingTiny),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// Representación visual interna para cada una de las opciones del LazyVerticalGrid
@Composable
fun TarjetaMenuMascota(item: MascotaMenu, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.cardMenuSmallHeight)
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = item.colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.paddingMicro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(item.tituloResId),
                color = TextoNegro,
                fontSize = Dimens.textSizeBodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = Dimens.lineHeightMedium
            )

            if (item.enMantenimiento) {
                Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                Text(
                    text = stringResource(R.string.menu_mantenimiento),
                    color = GrisOscuro,
                    fontSize = Dimens.textSizeMicro,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMascotasMenu() {
    val nav = rememberNavController()
    MascotasMenu(navController = nav)
}