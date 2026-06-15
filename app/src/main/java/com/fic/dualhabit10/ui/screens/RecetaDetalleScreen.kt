package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.AlimentacionViewModel
import com.fic.dualhabit10.ui.utils.traducirTexto
import com.fic.dualhabit10.ui.utils.traducirLista
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.GrisSeparador
import com.fic.dualhabit10.ui.theme.GrisTextoHint

// Vista analítica detallada que desglosa los ingredientes, aporte calórico y preparación de una receta
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaDetalleScreen(
    recetaId: Int,
    navController: NavController,
    viewModel: AlimentacionViewModel = viewModel()
) {
    val recetas by viewModel.recetas.collectAsState()

    val receta = recetas.find { it.id == recetaId }

    if (receta != null) {
        val colorFondo = try {
            Color(android.graphics.Color.parseColor(receta.colorHex))
        } catch (e: Exception) {
            GrisSeparador
        }

        Scaffold(
            topBar = {
                // Barra de navegación superior con color personalizado según la categoría culinaria
                TopAppBar(
                    title = { Text(stringResource(R.string.title_preparacion), fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.desc_volver),
                                tint = TextoNegro
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = colorFondo)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TextoBlanco)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Banner superior que ilustra el plato finalizado
                AsyncImage(
                    model = receta.imagenUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.imageBannerHeight),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(Dimens.spacerMedium)) {
                    // Título de la receta con localización dinámica bajo demanda
                    Text(text = traducirTexto(receta.nombre), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextoNegro)
                    Text(text = receta.calorias, fontSize = 16.sp, color = GrisTextoHint, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))
                    HorizontalDivider(color = GrisSeparador, thickness = Dimens.dividerThickness)
                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))

                    Text(text = stringResource(R.string.title_ingredientes), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoNegro)
                    Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                    // Desglose e iteración de la lista de insumos necesarios traducidos
                    traducirLista(receta.ingredientes).forEach { ingrediente ->
                        Text(
                            text = "* $ingrediente",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(vertical = Dimens.paddingMicro),
                            color = GrisOscuro
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.spacerMedium))

                    Text(text = stringResource(R.string.title_pasos_seguir), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoNegro)
                    Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                    // Secuencia cronológica indexada para la ejecución paso a paso del plato
                    traducirLista(receta.pasos).forEachIndexed { index, paso ->
                        Text(
                            text = "${index + 1}. $paso",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(vertical = Dimens.paddingTiny),
                            color = GrisOscuro
                        )
                    }
                }
            }
        }
    } else {
        // Estado de carga preventivo mientras se resuelven los flujos asíncronos del repositorio
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GrisOscuro)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecetaDetalleScreen() {
    val nav = rememberNavController()
    RecetaDetalleScreen(recetaId = 1, navController = nav)
}