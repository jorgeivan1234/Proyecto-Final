package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.ActividadFisicaViewModel
import com.fic.dualhabit10.ui.utils.traducirTexto
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.GrisSeparador
import com.fic.dualhabit10.ui.theme.RojoAlerta
import com.fic.dualhabit10.ui.theme.VerdeCompletado
import com.fic.dualhabit10.ui.theme.AmarilloFondo

// Pantalla que muestra los detalles de una actividad física
// Aquí vemos de qué trata la rutina y tenemos un temporizador interactivo para medir el tiempo de ejercicio
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadDetalleScreen(
    actividadId: Int,
    navController: NavController,
    viewModel: ActividadFisicaViewModel = viewModel()
) {
    // Obtenemos los datos de la actividad desde la base de datos y nos preparamos para cualquier cambio
    val actividad by viewModel.buscarActividadPorId(actividadId).collectAsState(initial = null)

    actividad?.let { data ->

        // Revisamos el texto del color para poder usarlo en el diseño sin errores
        // Si hay algún problema con el color que viene de la base de datos usamos uno de repuesto para que la app no falle
        val colorBarra = try {
            Color(android.graphics.Color.parseColor(data.colorHex))
        } catch (e: Exception) {
            NaranjaCabecera
        }

        // Armamos la estructura principal de la pantalla y la barra superior
        Scaffold(
            containerColor = VerdeFondoHabitos,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.title_detalle_rutina),
                            fontWeight = FontWeight.Bold,
                            fontSize = Dimens.textSizeLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Detenemos el temporizador al salir de la pantalla
                            // Esto evita que el tiempo siga corriendo escondido o que la app se vuelva lenta
                            viewModel.pausarOdetenerJuego()
                            navController.popBackStack()
                        }) {
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
                    .background(VerdeFondoHabitos)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {

                // Cargamos y mostramos la imagen principal de la actividad
                AsyncImage(
                    model = data.imagenUrl,
                    contentDescription = traducirTexto(data.titulo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.imageDogMedium),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(Dimens.paddingLarge)) {

                    Text(
                        text = traducirTexto(data.titulo),
                        fontSize = Dimens.textSizeTitleLarge,
                        fontWeight = FontWeight.Black,
                        color = TextoNegro
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                    // Pequeñas etiquetas con información rápida como cuánto dura y qué tan intenso es el ejercicio
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
                    ) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text(text = "${stringResource(R.string.label_duracion_detalle)} ${traducirTexto(data.duracion)}") },
                        )
                        SuggestionChip(
                            onClick = { },
                            label = { Text(text = "${stringResource(R.string.label_intensidad_detalle)} ${traducirTexto(data.intensidad)}") },
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))
                    HorizontalDivider(
                        thickness = Dimens.dividerThickness,
                        color = GrisSeparador
                    )
                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))

                    // Sección con el texto que explica paso a paso qué hacer en la rutina
                    Text(
                        text = stringResource(R.string.title_instrucciones),
                        fontSize = Dimens.textSizeBodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                    Text(
                        text = traducirTexto(data.descripcion),
                        fontSize = Dimens.textSizeBody,
                        color = GrisOscuro,
                        lineHeight = Dimens.lineHeightMedium
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingLarge))

                    // Tarjeta especial para iniciar el entrenamiento con el temporizador
                    // El contenido cambia dependiendo de si apenas vamos a empezar si estamos jugando o si ya terminamos
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(Dimens.borderThickness, colorBarra, RoundedCornerShape(Dimens.cornerRadiusFab)),
                        colors= CardDefaults.cardColors(containerColor = TextoBlanco),
                        shape = RoundedCornerShape(Dimens.cornerRadiusFab)
                    ) {
                        Column(
                            modifier = Modifier.padding(Dimens.spacerMedium),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
                        ) {
                            Text(
                                text = stringResource(R.string.title_modulo_reto),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = Dimens.textSizeBody,
                                color = GrisTextoHint
                            )

                            // Qué pasa cuando el tiempo está corriendo
                            if (viewModel.juegoEnProgreso) {
                                // Calculamos los minutos y segundos para mostrar el tiempo en pantalla como en un reloj digital
                                val min = viewModel.tiempoRestanteKey / 60
                                val seg = viewModel.tiempoRestanteKey % 60

                                Text(
                                    text = String.format("%02d:%02d", min, seg),
                                    fontSize = Dimens.textSizeTimer,
                                    fontWeight = FontWeight.Black,
                                    color = RojoAlerta
                                )
                                Text(
                                    text = stringResource(R.string.msg_haz_actividad),
                                    fontSize = Dimens.textSizeSmall,
                                    color = GrisTextoHint
                                )

                                Button(
                                    onClick = { viewModel.pausarOdetenerJuego() },
                                    colors = ButtonDefaults.buttonColors(containerColor = RojoAlerta)
                                ) {
                                    Icon(Icons.Default.Pause, contentDescription = null)
                                    Spacer(modifier = Modifier.width(Dimens.paddingSmallMedium))
                                    Text(stringResource(R.string.btn_cancelar_entrenamiento))
                                }
                            }

                            // Qué pasa cuando logramos terminar el tiempo del ejercicio
                            else if (viewModel.juegosCompletados) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AmarilloFondo,
                                    modifier = Modifier.size(Dimens.iconBackgroundSize)
                                )

                                Text(
                                    text = stringResource(R.string.title_reto_completado),
                                    fontSize = Dimens.textSizeSubtitle,
                                    fontWeight = FontWeight.Bold,
                                    color = VerdeCompletado
                                )

                                Text(
                                    text = stringResource(R.string.msg_puntos_acumulados, viewModel.puntosGanados),
                                    textAlign = TextAlign.Center,
                                    fontSize = Dimens.textSizeBodyLarge,
                                    color = GrisOscuro
                                )

                                Button(
                                    onClick = {
                                        // Limpiamos el texto que dice cuánto dura la actividad para quedarnos solo con el número
                                        // Por ejemplo pasamos de duracion_15m a solo 15 para que el reloj funcione bien al reiniciar
                                        val minutosLimpios = data.duracion.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 15
                                        viewModel.iniciarJuego(minutosLimpios)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = colorBarra)
                                ) {
                                    Text(stringResource(R.string.btn_jugar_nuevo), color = TextoNegro, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Qué pasa cuando recién entramos a la pantalla y todavía no empezamos el ejercicio
                            else {
                                Text(
                                    text = stringResource(R.string.msg_presiona_iniciar),
                                    textAlign = TextAlign.Center,
                                    fontSize = Dimens.textSizeMedium,
                                    color = GrisOscuro
                                )

                                Button(
                                    onClick = {
                                        // Limpiamos el texto de la duración igual que arriba para usar solo los números al iniciar el tiempo
                                        val minutosLimpios = data.duracion.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 15
                                        viewModel.iniciarJuego(minutosLimpios)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = colorBarra)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextoNegro)
                                    Spacer(modifier = Modifier.width(Dimens.paddingSmallMedium))
                                    Text(stringResource(R.string.btn_iniciar_reto), color = TextoNegro, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.spacerMedium))

                    // Tarjeta de aviso para darle un buen consejo de hidratación al usuario
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorBarra.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(Dimens.cornerRadiusDefault)
                    ) {
                        Text(
                            text = stringResource(R.string.msg_recuerda_agua),
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