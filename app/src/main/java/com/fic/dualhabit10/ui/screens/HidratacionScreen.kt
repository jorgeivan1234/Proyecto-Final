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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HidratacionViewModel
import kotlinx.coroutines.launch

@Composable
fun HidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
) {
    // Conversión de ML a L para que la tarjeta de progreso sea más fácil
    val litrosConsumidos = viewModel.aguaConsumidaML / 1000f
    val litrosMeta = viewModel.metaDiariaML / 1000f

    // Preparación del cajón lateral y las corrutinas para poder abrir el menú sin trabar la pantalla
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BaseCustomDrawer(navController = navController, drawerState = drawerState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
        ) {

            // Cabecera naranja
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFFF7A22),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Posicionamiento dentro de la Box para que no sé mueva el botón de menú y el título quede central
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
                            tint = Color.Black,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 30.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.title_hidratacion),
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Fila para los accesos secundarios: Historial del lado izquierdo, perrito del lado derecho
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF81D4FA), shape = RoundedCornerShape(24.dp))
                            .clickable { navController.navigate("historial") }
                            .padding(horizontal = 36.dp, vertical = 18.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.btn_historial),
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.img_bol_perro),
                        contentDescription = stringResource(R.string.desc_hidratacion_mascota),
                        modifier = Modifier
                            .size(150.dp)
                            .clickable { navController.navigate("hidratacion_mascota") },
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Cuerpo principal de la pantalla donde se divide en 2 columnas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                // Columna izquierda: Botones para registrar agua y acceso a la calculadora
                Column(
                    modifier = Modifier.weight(1.2f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    BotonConsumoOvalado(
                        texto = stringResource(R.string.btn_add_250ml),
                        imagenRes = R.drawable.img_vaso,
                        onClick = { viewModel.agregarAgua(250) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BotonConsumoOvalado(
                        texto = stringResource(R.string.btn_add_500ml),
                        imagenRes = R.drawable.img_termo,
                        onClick = { viewModel.agregarAgua(500) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BotonConsumoOvalado(
                        texto = stringResource(R.string.btn_add_1l),
                        imagenRes = R.drawable.img_jarra,
                        onClick = { viewModel.agregarAgua(1000) }
                    )
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = stringResource(R.string.label_configurar_parametros),
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.img_calculadora),
                        contentDescription = stringResource(R.string.desc_calculadora),
                        modifier = Modifier
                            .size(130.dp)
                            .clickable { navController.navigate("calculadora_agua") },
                        contentScale = ContentScale.Fit
                    )
                }

                // Columna derecha: Visualización de la botella grande y la etiqueta el tu progreso
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
                            .height(340.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    // Tarjeta flotante de color azul que contiene el progreso
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF448AFF), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.label_llevas_agua,
                                litrosConsumidos,
                                litrosMeta
                            ),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

// Extraemos el diseño del botón de componente reutilizable
@Composable
fun BotonConsumoOvalado(
    texto: String,
    imagenRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .background(Color(0xFFB3E5FC), shape = RoundedCornerShape(50.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = imagenRes),
                contentDescription = texto,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = texto,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHidratacion(){
    HidratacionScreen(navController = rememberNavController())
}