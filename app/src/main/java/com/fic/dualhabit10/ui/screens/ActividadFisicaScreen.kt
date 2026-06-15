package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.data.local.ActividadFisicaEntity
import com.fic.dualhabit10.ui.viewmodels.ActividadFisicaViewModel
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.utils.traducirTexto
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.GrisSeparador

// Pantalla principal donde mostramos todas las actividades físicas disponibles
// Aquí el usuario puede ver la lista de rutinas y moverse por las diferentes opciones
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadFisicaScreen(
    navController: NavController,
    viewModel: ActividadFisicaViewModel = viewModel()
) {
    // Preparamos los datos que vamos a mostrar y las herramientas para abrir menús y cambiar de pantalla
    val listaActividades by viewModel.actividades.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Armamos la pantalla completa incluyendo el menú lateral oculto
    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            containerColor = VerdeFondoHabitos,
            contentWindowInsets = WindowInsets(Dimens.paddingZero),
            floatingActionButton = {

                // Botón flotante que resalta en la pantalla para buscar parques en el mapa
                ExtendedFloatingActionButton(
                    onClick = { abrirMapaParquesMascotas(context) },
                    containerColor = NaranjaCabecera,
                    contentColor = TextoBlanco,
                    elevation = FloatingActionButtonDefaults.elevation(Dimens.elevationFab),
                    shape = RoundedCornerShape(Dimens.cornerRadiusFab),
                    modifier = Modifier.padding(bottom = Dimens.paddingDefault)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.desc_mapa),
                        modifier = Modifier.size(Dimens.iconSizeMedium)
                    )
                    Spacer(modifier = Modifier.width(Dimens.paddingSmall))
                    Text(text = stringResource(R.string.btn_parques_cercanos), fontWeight = FontWeight.Bold)
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->

            // Espacio principal donde colocamos todo el contenido ajustando los bordes para que no se corte nada
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .background(VerdeFondoHabitos)
            ) {

                // Parte superior de la pantalla con su fondo de color y el botón del menú
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
                        .padding(top = Dimens.paddingExtraLarge),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.paddingSmall),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // Abrimos el menú lateral de forma suave sin que la aplicación se trabe
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.desc_menu),
                                tint = TextoNegro,
                                modifier = Modifier.size(Dimens.iconSizeExtraLarge),
                            )
                        }

                        // Un pequeño cartelito llamativo para que el usuario sepa en qué sección está
                        Box(
                            modifier = Modifier
                                .background(
                                    color = AmarilloFondo,
                                    shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                                )
                                .padding(
                                    horizontal = Dimens.paddingMedium,
                                    vertical = Dimens.paddingSmallMedium
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.title_actividad_fisica),
                                textAlign = TextAlign.Center,
                                fontSize = Dimens.textSizeLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextoNegro
                            )
                        }

                        Spacer(modifier = Modifier.size(Dimens.spacerHeader))
                    }
                }

                // Mostramos la lista de actividades cargando solo las que se ven en pantalla para ahorrar memoria y batería
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = Dimens.paddingDefault,
                            vertical = Dimens.paddingMedium
                        ),
                    verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault),
                    contentPadding = PaddingValues(bottom = Dimens.paddingBottomFab)
                ) {
                    items(listaActividades, key = { it.id }) { actividad ->
                        TarjetaActividad(
                            actividad = actividad,
                            onClick = { navController.navigate("actividad_detalle/${actividad.id}") }
                        )
                    }
                }
            }
        }
    }
}

// Diseño de la tarjeta individual que muestra la información de cada actividad en la lista
@Composable
fun TarjetaActividad(actividad: ActividadFisicaEntity, onClick: () -> Unit ) {

    // Revisamos el texto del color y lo convertimos a un color real
    // Si hay un error le ponemos color blanco para que no pase nada malo
    val colorFondo = try {
        Color(android.graphics.Color.parseColor(actividad.colorHex))
    } catch (e: Exception) {
        TextoBlanco
    }

    // Creamos la tarjeta y le decimos que cuando la toquen abra la pantalla con todos los detalles
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.cornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.borderThickness),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.paddingMediumSmall)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
        ) {

            // Mostramos el nombre de la actividad traduciéndolo al idioma del usuario al instante
            Text(
                text = traducirTexto(actividad.titulo),
                fontSize = Dimens.textSizeLarge,
                fontWeight = FontWeight.Bold,
                color = TextoNegro,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Dimens.paddingTiny)
                ) {
                    Text(
                        text = traducirTexto(actividad.descripcion),
                        fontSize = Dimens.textSizeSmall,
                        color = GrisOscuro,
                        lineHeight = Dimens.lineHeightSmall
                    )
                }

                // Cargamos la imagen de la actividad recortándola un poco para que entre perfecto en el cuadrito
                Card(
                    modifier = Modifier.size(Dimens.imageHabitoSize),
                    shape = RoundedCornerShape(Dimens.cornerRadiusSmall),
                    colors = CardDefaults.cardColors(containerColor = TextoBlanco)
                ) {
                    AsyncImage(
                        model = actividad.imagenUrl,
                        contentDescription = traducirTexto(actividad.titulo),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            HorizontalDivider(
                color = GrisSeparador,
                thickness = Dimens.dividerThickness
            )

            // Mostramos de forma rápida cuánto dura el ejercicio y qué tan cansado será
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_duracion),
                        fontSize = Dimens.textSizeMicro,
                        fontWeight = FontWeight.ExtraBold,
                        color = GrisTextoHint
                    )
                    Text(
                        text = traducirTexto(actividad.duracion),
                        fontSize = Dimens.textSizeMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.label_intensidad),
                        fontSize = Dimens.textSizeMicro,
                        fontWeight = FontWeight.ExtraBold,
                        color = GrisTextoHint
                    )
                    Text(
                        text = traducirTexto(actividad.intensidad),
                        fontSize = Dimens.textSizeMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )
                }
            }
        }
    }
}

// Función para abrir la aplicación de mapas del celular y buscar lugares para pasear al perrito
private fun abrirMapaParquesMascotas(context: Context) {

    // Le decimos al mapa exactamente lo que queremos buscar para ahorrarle tiempo al usuario
    val gmmIntentUri = Uri.parse("geo:0,0?q=parque+para+mascota")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }

    // Revisamos si el celular tiene Google Maps instalado para abrirlo directamente ahí
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // Si no tiene la aplicación abrimos el navegador de internet para no dejar al usuario sin opciones
        val navegadorIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=parque+para+mascota"))
        context.startActivity(navegadorIntent)
    }
}