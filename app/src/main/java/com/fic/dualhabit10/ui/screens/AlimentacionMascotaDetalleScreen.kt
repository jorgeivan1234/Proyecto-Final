package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.fic.dualhabit10.ui.utils.traducirTexto
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AlimentacionMascotaViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.BlancoFondo
import com.fic.dualhabit10.ui.theme.GrisOscuro

// Pantalla que muestra los detalles de la comida para mascotas
// Aquí vemos la información de la receta y los avisos importantes del veterinario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentacionMascotaDetalleScreen(
    recetaId: Int,
    navController: NavController,
    viewModel: AlimentacionMascotaViewModel = viewModel()
) {
    // Obtenemos los datos de la receta desde la base de datos y reaccionamos si hay cambios
    val receta by viewModel.buscarRecetaPorId(recetaId).collectAsState(initial = null)
    val context = LocalContext.current

    receta?.let { data ->

        // Limpiamos y convertimos el texto del color para poder usarlo en el diseño sin errores
        // Le agregamos el símbolo '#' si le falta para que la aplicación no se cierre de golpe
        val colorString = if (data.colorHex.startsWith("#")) data.colorHex else "#${data.colorHex}"
        val colorBarra = try {
            Color(android.graphics.Color.parseColor(colorString))
        } catch (e: Exception) {
            NaranjaCabecera
        }

        // Usamos el color de fondo estándar que definimos para toda la aplicación
        val colorFondoPantalla = VerdeFondoHabitos

        // Preparamos la imagen para mostrarla en pantalla
        // Revisamos si la imagen viene de internet o si ya está guardada dentro de la app
        val imageResId = context.resources.getIdentifier(
            data.imagenUrl,
            "drawable",
            context.packageName
        )
        val modeloImagen = if (imageResId != 0) imageResId else data.imagenUrl

        // Armamos la estructura principal de la pantalla y la barra superior para volver atrás
        Scaffold(
            containerColor = colorFondoPantalla,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.title_detalle_rutina),
                            fontWeight = FontWeight.Bold,
                            fontSize = Dimens.textSizeLarge,
                            color = TextoNegro
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.desc_volver),
                                tint = TextoNegro
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = colorBarra)
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorFondoPantalla)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {

                // Cargamos y mostramos la imagen principal de la receta ajustando su tamaño
                AsyncImage(
                    model = modeloImagen,
                    contentDescription = traducirTexto(data.nombre),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.imageHeightDetail)
                        .background(BlancoFondo),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(Dimens.paddingExtraLarge)) {

                    // Mostramos el nombre de la receta y aplicamos la traducción al momento
                    Text(
                        text = traducirTexto(data.nombre),
                        fontSize = Dimens.textSizeTitleLarge,
                        fontWeight = FontWeight.Black,
                        color = TextoNegro
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                    // Pequeñas etiquetas con información rápida como para qué mascota es y sus calorías
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.paddingMedium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SuggestionChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = stringResource(R.string.format_apto_para, traducirTexto(data.TipoMascota)),
                                    color = TextoNegro
                                )
                            },
                        )
                        SuggestionChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = traducirTexto(data.calorias),
                                    color = TextoNegro
                                )
                            },
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))
                    HorizontalDivider(
                        thickness = Dimens.dividerThickness,
                        color = GrisOscuro.copy(alpha = 0.12f)
                    )
                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))

                    // Sección con el texto que explica los detalles de la dieta
                    Text(
                        text = stringResource(R.string.title_descripcion_dieta),
                        fontSize = Dimens.textSizeBodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                    Text(
                        text = traducirTexto(data.descripcion),
                        fontSize = Dimens.textSizeMedium,
                        color = GrisOscuro,
                        lineHeight = Dimens.lineHeightMedium
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingXXLarge))

                    // Tarjeta de aviso para recordarle al usuario que debe consultar a su veterinario
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorBarra.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                    ) {
                        Text(
                            text = stringResource(R.string.msg_aviso_veterinario),
                            modifier = Modifier.padding(Dimens.paddingDefault),
                            fontSize = Dimens.textSizeMedium,
                            fontWeight = FontWeight.Medium,
                            color = TextoNegro,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}