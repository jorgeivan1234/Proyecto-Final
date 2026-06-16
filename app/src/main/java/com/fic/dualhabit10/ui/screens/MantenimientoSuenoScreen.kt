package com.fic.dualhabit10.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.data.local.SuenoEntity
import com.fic.dualhabit10.ui.viewmodels.SuenoViewModel
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.AzulFondoNoche1
import com.fic.dualhabit10.ui.theme.AzulFondoNoche2
import com.fic.dualhabit10.ui.theme.AzulFondoNoche3
import com.fic.dualhabit10.ui.theme.AzulClaroLuna
import com.fic.dualhabit10.ui.theme.AmarilloSol
import com.fic.dualhabit10.ui.theme.RojoDetener
import com.fic.dualhabit10.ui.theme.BlancoTransparente08
import com.fic.dualhabit10.ui.theme.BlancoTransparente12
import com.fic.dualhabit10.ui.theme.NaranjaTransparente20

// Pantalla para monitorear, iniciar y registrar el ciclo de sueño del usuario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantenimientoSuenoScreen(
    navController: NavController,
    viewModel: SuenoViewModel = viewModel()
) {
    val estaDurmiendo = viewModel.estaDurmiendo
    val tiempoMs = viewModel.tiempoTranscurridoMs
    val historial by viewModel.historialSueno.collectAsState()

    var mostrarDialogoAnimo by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val segundos = (tiempoMs / 1000) % 60
    val minuto = (tiempoMs / (1000 * 60)) % 60
    val hora = (tiempoMs / (1000 * 60 * 60)) % 24
    val tiempoText = String.format("%02d:%02d:%02d", hora, minuto, segundos)

    val tipsMascotas = stringArrayResource(R.array.tips_mascotas_sueno)
    val tipAleatorio = remember { tipsMascotas.random() }

    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = NaranjaCabecera,
                            shape = RoundedCornerShape(
                                bottomStart = Dimens.cornerRadiusMedium,
                                bottomEnd = Dimens.cornerRadiusMedium
                            )
                        )
                        .statusBarsPadding()
                        .padding(
                            start = Dimens.paddingMedium,
                            end = Dimens.paddingMedium,
                            bottom = Dimens.spacerMedium,
                            top = Dimens.paddingExtraLarge
                        )
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.desc_menu),
                                tint = TextoNegro,
                                modifier = Modifier.size(Dimens.iconSizeLarge)
                            )
                        }

                        Text(
                            text = stringResource(R.string.title_modo_sueno),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextoNegro,
                            modifier = Modifier
                                .background(
                                    color = AmarilloFondo,
                                    shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
                                )
                                .padding(horizontal = Dimens.spacerMedium, vertical = Dimens.paddingSmallMedium)
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(AzulFondoNoche1, AzulFondoNoche2, AzulFondoNoche3)
                        )
                    )
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Dimens.spacerMedium))

                Box(
                    modifier = Modifier
                        .size(Dimens.indicadorSuenoSize)
                        .background(color = BlancoTransparente08, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (estaDurmiendo) Icons.Default.Bedtime else Icons.Default.WbSunny,
                        contentDescription = stringResource(R.string.desc_estado_sueno),
                        tint = if (estaDurmiendo) AzulClaroLuna else AmarilloSol,
                        modifier = Modifier.size(Dimens.iconSizeHuge)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.paddingLarge))

                Text(
                    text = if (estaDurmiendo) tiempoText else stringResource(R.string.text_listo_descansar),
                    color = TextoBlanco,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (estaDurmiendo) stringResource(R.string.text_registrando_sueno) else stringResource(R.string.text_presiona_dormir),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Dimens.paddingSmall)
                )

                Spacer(modifier = Modifier.height(Dimens.paddingXXLarge))

                Button(
                    onClick = {
                        if (estaDurmiendo) {
                            mostrarDialogoAnimo = true
                        } else {
                            viewModel.iniciarDescanso()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estaDurmiendo) RojoDetener else NaranjaCabecera
                    ),
                    shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.buttonHeightLarge)
                ) {
                    Text(
                        text = if (estaDurmiendo) stringResource(R.string.btn_despertar) else stringResource(R.string.btn_iniciar_descanso),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoBlanco
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.spacerLarge))

                AnimatedVisibility(visible = historial.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.title_ultimas_noches),
                            color = TextoBlanco,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingMedium),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(historial) { registro ->
                                TarjetaHistorialSueno(registro)
                            }
                        }
                        Spacer(modifier = Modifier.height(Dimens.spacerLarge))
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BlancoTransparente12),
                    shape = RoundedCornerShape(Dimens.cornerRadiusDefault)
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens.spacerMedium),
                        verticalArrangement = Arrangement.spacedBy(Dimens.spacerSmall)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(Dimens.iconBackgroundSize)
                                    .background(
                                        color = NaranjaTransparente20,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = stringResource(R.string.desc_tips_mascota),
                                    tint = NaranjaCabecera,
                                    modifier = Modifier.size(Dimens.iconSizeMedium)
                                )
                            }
                            Spacer(modifier = Modifier.width(Dimens.paddingDefault))
                            Text(
                                text = stringResource(R.string.title_descanso_mascota),
                                color = TextoBlanco,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = tipAleatorio,
                            color = Color.LightGray,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    if (mostrarDialogoAnimo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAnimo = false },
            title = {
                Text(
                    text = stringResource(R.string.dialog_title_despertar),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.dialog_desc_despertar),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingSmall),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("😫", "😐", "😊", "⚡").forEach { emoji ->
                        TextButton(
                            onClick = {
                                viewModel.terminarDescanso(emoji)
                                mostrarDialogoAnimo = false
                            }
                        ) {
                            Text(text = emoji, fontSize = 34.sp)
                        }
                    }
                }
            },
            shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge),
        )
    }
}

// Representación visual interna para cada registro del historial de descanso
@Composable
fun TarjetaHistorialSueno(registro: SuenoEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = TextoBlanco),
        shape = RoundedCornerShape(Dimens.cornerRadiusDefault),
        modifier = Modifier.width(Dimens.tarjetaHistorialWidth)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.paddingMediumSmall)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmallMedium)
        ) {
            Text(text = registro.estadoAnimo, fontSize = 28.sp)
            Text(
                text = stringResource(R.string.text_horas_dormidas, registro.horasDormidas),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextoNegro
            )
            Text(
                text = registro.fecha,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMantenimientoSuenoScreen() {
    val nav = rememberNavController()
    MantenimientoSuenoScreen(navController = nav)
}