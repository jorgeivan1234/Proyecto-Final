package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.AzulBotonClaro
import com.fic.dualhabit10.ui.theme.AzulHidratacion
import com.fic.dualhabit10.ui.theme.AzulTarjetaProgreso

// Constantes
private object HidratacionRoutes {
    const val HISTORIAL = "historial"
    const val MASCOTA = "hidratacion_mascota"
    const val CALCULADORA = "calculadora_agua"
    const val REGRESO_HABITOS = "hid_habitos"
}

@Composable
fun HidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
) {
    // Conversión de ML a L para que la tarjeta de progreso sea más fácil
    val litrosConsumidos = viewModel.aguaConsumidaML / 1000f
    val litrosMeta = viewModel.metaDiariaML / 1000f

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BaseCustomDrawer(navController = navController, drawerState = drawerState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
        ) {

            // Cabecera naranja
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
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Posicionamiento de Botón menú y Título
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
                    Box(
                        modifier = Modifier
                            .background(AmarilloFondo, shape = RoundedCornerShape(Dimens.cornerRadiusLarge))
                            .padding(horizontal = Dimens.paddingXXLarge, vertical = Dimens.paddingSmall)
                    ) {
                        Text(
                            text = stringResource(R.string.title_hidratacion),
                            color = TextoNegro,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                // Fila de accesos secundarios: Historial y Perrito
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(AzulBotonClaro, shape = RoundedCornerShape(Dimens.cornerRadiusMedium))
                            .clickable { navController.navigate(HidratacionRoutes.HISTORIAL) }
                            .padding(horizontal = Dimens.paddingXXLarge, vertical = Dimens.paddingLarge)
                    ) {
                        Text(
                            text = stringResource(R.string.btn_historial),
                            color = TextoNegro,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.img_bol_perro),
                        contentDescription = stringResource(R.string.desc_hidratacion_mascota),
                        modifier = Modifier
                            .size(Dimens.imagePetBowlSize)
                            .clickable { navController.navigate(HidratacionRoutes.MASCOTA) },
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Cuerpo principal dividido en 2 columnas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.layoutHidratacionHeight)
                    .padding(horizontal = Dimens.spacerMedium, vertical = Dimens.paddingDefault),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                // Columna izquierda: Registro de agua y Calculadora
                Column(
                    modifier = Modifier.weight(1.2f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                    BotonConsumoOvalado(
                        texto = stringResource(R.string.btn_add_250ml),
                        imagenRes = R.drawable.img_vaso,
                        onClick = { viewModel.agregarAgua(250) }
                    )
                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))
                    BotonConsumoOvalado(
                        texto = stringResource(R.string.btn_add_500ml),
                        imagenRes = R.drawable.img_termo,
                        onClick = { viewModel.agregarAgua(500) }
                    )
                    Spacer(modifier = Modifier.height(Dimens.paddingDefault))
                    BotonConsumoOvalado(
                        texto = stringResource(R.string.btn_add_1l),
                        imagenRes = R.drawable.img_jarra,
                        onClick = { viewModel.agregarAgua(1000) }
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacerLarge))

                    Text(
                        text = stringResource(R.string.label_configurar_parametros),
                        color = TextoNegro,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                    Image(
                        painter = painterResource(id = R.drawable.img_calculadora),
                        contentDescription = stringResource(R.string.desc_calculadora),
                        modifier = Modifier
                            .size(Dimens.imageCalculadoraSize)
                            .clickable { navController.navigate(HidratacionRoutes.CALCULADORA) },
                        contentScale = ContentScale.Fit
                    )
                }

                // Columna derecha: Visualización de botella y meta
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hidratacion),
                        contentDescription = stringResource(R.string.desc_botella_agua),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.imageBotellaHeight),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(Dimens.paddingMedium))

                    // Tarjeta flotante de progreso
                    Box(
                        modifier = Modifier
                            .background(AzulTarjetaProgreso, shape = RoundedCornerShape(Dimens.cornerRadiusSmall))
                            .padding(horizontal = Dimens.paddingDefault, vertical = Dimens.paddingDefault)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.label_llevas_agua,
                                litrosConsumidos,
                                litrosMeta
                            ),
                            color = TextoBlanco,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.spacerExtraLarge))

            // Botón de regresar inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.paddingDefault)
                    .background(NaranjaCabecera, shape = RoundedCornerShape(Dimens.cornerRadiusLarge))
                    .clickable { navController.navigate(HidratacionRoutes.REGRESO_HABITOS) }
                    .padding(vertical = Dimens.paddingMedium),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = stringResource(R.string.desc_regresar),
                    color = TextoBlanco,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Componente reutilizable local
@Composable
fun BotonConsumoOvalado(
    texto: String,
    imagenRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(Dimens.buttonOvalWidth)
            .background(AzulHidratacion, shape = RoundedCornerShape(Dimens.cornerRadiusLarge))
            .clickable { onClick() }
            .padding(horizontal = Dimens.paddingDefault, vertical = Dimens.paddingMedium),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = imagenRes),
                contentDescription = texto,
                modifier = Modifier.size(Dimens.iconSizeExtraLarge),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(Dimens.spacerSmall))
            Text(
                text = texto,
                color = TextoNegro,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}