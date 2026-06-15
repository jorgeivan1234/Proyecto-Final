package com.fic.dualhabit10.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.fic.dualhabit10.ui.utils.traducirTexto
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.data.local.AlimentacionMascotaEntity
import com.fic.dualhabit10.ui.viewmodels.AlimentacionMascotaViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.BlancoFondo
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos

// Pantalla donde mostramos la lista de recetas y dietas para nuestras mascotas
// Aquí el usuario puede ver todas las opciones de comida y tocar una para conocer más detalles
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentacionMascotaScreen(
    navController: NavController,
    viewModel: AlimentacionMascotaViewModel = viewModel()
) {
    // Obtenemos la lista de recetas desde la base de datos y estamos atentos a cualquier cambio
    val recetas by viewModel.recetas.collectAsState()

    // Guardamos información del celular que la aplicación necesita para buscar imágenes o textos
    val context = LocalContext.current

    // Armamos la estructura de la pantalla empezando por la barra superior
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Un pequeño cartel centrado con el título de la pantalla para que el usuario sepa dónde está
                    Box(
                        modifier = Modifier
                            .background(
                                color = AmarilloFondo,
                                shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                            )
                            .padding(
                                horizontal = Dimens.paddingLarge,
                                vertical = Dimens.paddingSmallMedium
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.title_dieta_mascotas),
                            fontWeight = FontWeight.Bold,
                            fontSize = Dimens.textSizeBody,
                            color = TextoNegro
                        )
                    }
                },
                navigationIcon = {
                    // Botón con una flechita para regresar a la pantalla anterior sin perderse
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_volver),
                            tint = TextoNegro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NaranjaCabecera)
            )
        }
    ) { padding ->

        // Mostramos la lista de recetas cargando solo las que caben en la pantalla para que el celular no se ponga lento
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
                .padding(padding),
            contentPadding = PaddingValues(Dimens.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingDefault)
        ) {
            items(items = recetas) { receta: AlimentacionMascotaEntity ->

                // Revisamos el texto del color y le agregamos un símbolo de número al inicio si le falta
                val colorString = if (receta.colorHex.startsWith("#")) receta.colorHex else "#${receta.colorHex}"

                // Intentamos convertir ese texto en un color real para pintar la tarjeta
                // Si algo sale mal le ponemos un color amarillo de repuesto para que la aplicación no se cierre
                val colorFondoCard = try {
                    Color(android.graphics.Color.parseColor(colorString))
                } catch (_: Exception) {
                    AmarilloFondo
                }

                // Buscamos si la imagen de la receta ya viene guardada dentro de la aplicación
                // Si no la encontramos así entonces usamos la dirección web que traiga para descargarla
                val imageResId = context.resources.getIdentifier(
                    receta.imagenUrl,
                    "drawable",
                    context.packageName
                )
                val modeloImagen = if (imageResId != 0) imageResId else receta.imagenUrl

                // Creamos la tarjeta que envuelve a cada receta y le decimos que al tocarla nos lleve a ver los detalles
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("receta_mascota_detalle/${receta.id}")
                        },
                    shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge),
                    colors = CardDefaults.cardColors(containerColor = colorFondoCard),
                ) {
                    Row(
                        modifier = Modifier.padding(Dimens.paddingDefault),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Cargamos la imagen de la comida y la recortamos para que quede bonita dentro de su espacio
                        AsyncImage(
                            model = modeloImagen,
                            contentDescription = traducirTexto(receta.nombre),
                            modifier = Modifier
                                .size(Dimens.imageHabitoSize)
                                .background(
                                    color = BlancoFondo,
                                    shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(Dimens.paddingDefault))

                        Column {
                            // Pequeña etiqueta para saber a qué mascota va dirigida esta receta
                            SuggestionChip(
                                onClick = { },
                                label = {
                                    // Traducimos el tipo de mascota al idioma del usuario
                                    // Si por alguna razón ese dato viene vacío mostramos un texto genérico
                                    val tipoTraducido = traducirTexto(receta.TipoMascota)
                                    val textoMascota = tipoTraducido.ifEmpty {
                                        stringResource(R.string.label_mascota_default)
                                    }
                                    Text(
                                        text = textoMascota,
                                        fontSize = Dimens.textSizeMicro,
                                        fontWeight = FontWeight.Bold,
                                        color = TextoNegro
                                    )
                                },
                                shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                            )

                            // Mostramos el nombre de la receta ya traducido
                            Text(
                                text = traducirTexto(receta.nombre),
                                fontWeight = FontWeight.Bold,
                                fontSize = Dimens.textSizeBodyLarge,
                                color = TextoNegro
                            )

                            // Mostramos la cantidad de calorías que tiene esta comida en el idioma del usuario
                            Text(
                                text = traducirTexto(receta.calorias),
                                fontSize = Dimens.textSizeMedium,
                                color = GrisOscuro,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(Dimens.paddingTiny))

                            // Agregamos una breve descripción de la comida limitándola a solo dos líneas para que no ocupe tanto espacio
                            Text(
                                text = traducirTexto(receta.descripcion),
                                fontSize = Dimens.textSizeSmall,
                                lineHeight = Dimens.lineHeightSmall,
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