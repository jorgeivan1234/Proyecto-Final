package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AlimentacionViewModel
import com.fic.dualhabit10.ui.utils.traducirTexto
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.GrisOscuro

// Pantalla donde mostramos la lista de recetas saludables para las personas
// Aquí el usuario puede ver opciones deliciosas y tocar una para conocer los pasos para prepararla
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentacionScreen(
    navController: NavController,
    viewModel: AlimentacionViewModel = viewModel()
) {
    // Obtenemos la lista de recetas desde nuestra base de datos para mostrarlas en pantalla
    val recetas by viewModel.recetas.collectAsState()

    // Guardamos las herramientas del celular que necesitamos para abrir mapas o buscar recursos
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Armamos un cuadrito amarillo centrado para que el título de la pantalla resalte bonito
                    Box(
                        modifier = Modifier
                            .background(AmarilloFondo, shape = RoundedCornerShape(Dimens.cornerRadiusLarge))
                            .padding(horizontal = Dimens.spacerMedium, vertical = Dimens.paddingSmallMedium)
                    ) {
                        Text(
                            text = stringResource(R.string.title_recetas),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextoNegro
                        )
                    }
                },
                navigationIcon = {
                    // Botón con una flecha para regresar fácilmente a la pantalla anterior
                    IconButton( onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_atras),
                            tint = TextoNegro
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NaranjaCabecera)
            )
        },
        floatingActionButton = {
            // Botón flotante llamativo en la parte de abajo para buscar restaurantes con comida sana
            ExtendedFloatingActionButton(
                onClick = { abrirMapaRestaurantesSalusables(context) },
                containerColor = NaranjaCabecera,
                contentColor = TextoBlanco,
                elevation = FloatingActionButtonDefaults.elevation(Dimens.elevationFab),
                shape = RoundedCornerShape(Dimens.cornerRadiusFab),
                modifier = Modifier.padding(Dimens.paddingDefault),
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = stringResource(R.string.desc_mapa))
                Spacer(modifier = Modifier.width(Dimens.paddingSmall))
                Text(text = stringResource(R.string.btn_restaurantes_cercanos), fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->

        // Mostramos la lista de recetas cargando solo las que caben en la pantalla para que todo fluya súper rápido
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
                .padding(padding),
            contentPadding = PaddingValues(
                start = Dimens.paddingDefault,
                end = Dimens.paddingDefault,
                top = Dimens.paddingDefault,
                bottom = Dimens.paddingBottomFab
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault)
        ) {
            items(items = recetas) { receta ->

                // Convertimos el texto del color de la receta en un color real para pintar el fondo
                val colorFondoCard = Color(android.graphics.Color.parseColor(receta.colorHex))

                // Creamos una tarjeta para cada receta y le decimos que al tocarla nos lleve a ver los detalles
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.cornerRadiusDefault),
                    colors = CardDefaults.cardColors(containerColor = colorFondoCard),
                    onClick = { navController.navigate("receta_detalle/${receta.id}") }
                ) {
                    Row(
                        modifier = Modifier.padding(Dimens.paddingDefault),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Cargamos la foto del platillo y le aplicamos una traducción por si usan lectores de pantalla
                        AsyncImage(
                            model = receta.imagenUrl,
                            contentDescription = traducirTexto(receta.nombre),
                            modifier = Modifier
                                .size(Dimens.imageHabitoSize)
                                .background(TextoBlanco, shape = RoundedCornerShape(Dimens.cornerRadiusSmall)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(Dimens.paddingDefault))

                        Column {
                            // Mostramos el nombre de la receta traducido al idioma del usuario
                            Text(
                                text = traducirTexto(receta.nombre),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextoNegro
                            )

                            // Mostramos cuántas calorías tiene el platillo
                            Text(
                                text = receta.calorias,
                                fontSize = 14.sp,
                                color = GrisOscuro,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(Dimens.paddingTiny))

                            // Mostramos una probadita de la descripción traducida y la limitamos a dos líneas para no ocupar tanto lugar
                            Text(
                                text = traducirTexto(receta.descripcion),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                maxLines = 2,
                                color = TextoNegro
                            )
                        }
                    }
                }
            }
        }
    }
}

// Función para abrir la aplicación de mapas del celular y buscar lugares ricos para comer sano
fun abrirMapaRestaurantesSalusables(context: Context) {

    // Le pedimos al mapa que busque directamente restaurantes con comida nutritiva
    val intentUri = Uri.parse("geo:0,0?q=restaurantes+saludables+comida+nutritiva")
    val mapIntent = Intent(Intent.ACTION_VIEW, intentUri).apply {
        setPackage("com.google.android.apps.maps")
    }

    // Revisamos si el celular tiene la aplicación de mapas instalada para abrirla ahí mismo
    if(mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // Si no hay mapas instalados abrimos el navegador de internet para que el usuario no se quede sin opciones
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=restaurantes+saludables+comida+nutritiva"))
        context.startActivity(browserIntent)
    }
}