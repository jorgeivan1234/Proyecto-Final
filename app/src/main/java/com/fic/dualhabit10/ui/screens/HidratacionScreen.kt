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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import java.util.Locale
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch

@Composable
fun HidratacionScreen(
    navController: NavController,
    viewModel: HidratacionViewModel = viewModel()
 ) {
    //conversion directa a litros para cada caja
    val litrosConsumidos = viewModel.aguaConsumidaML / 1000f
    val litrosMeta = viewModel.metaDiariaML / 1000f
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BaseCustomDrawer(navController = navController, drawerState = drawerState) {

        //barra superior
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFFF7A22),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            scope.launch { drawerState.open() } },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir Menu",
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
                            text = "Hidratacion",
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF81D4FA), shape = RoundedCornerShape(24.dp))
                            .clickable {/* ruta futra*/ }
                            .padding(horizontal = 36.dp, vertical = 18.dp)
                    ) {
                        Text(
                            text = "Historial",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.img_bol_perro),
                        contentDescription = "Cuenco de agua",
                        modifier = Modifier.size(150.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // contenedor principal
                Column(
                    modifier = Modifier.weight(1.2f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    //botones
                    BotonConsumoOvalado(
                        texto = "+250ml",
                        imagenRes = R.drawable.img_vaso,
                        onClick = { viewModel.agregarAgua(250) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BotonConsumoOvalado(
                        texto = "+500ml",
                        imagenRes = R.drawable.img_termo,
                        onClick = { viewModel.agregarAgua(500) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BotonConsumoOvalado(
                        texto = "+1L",
                        imagenRes = R.drawable.img_jarra,
                        onClick = { viewModel.agregarAgua(1000) }
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    Text(
                        text = "Configurar parametros\nde hidratacion",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.img_calculadora),
                        contentDescription = "Calculadora",
                        modifier = Modifier
                            .size(130.dp)
                            .clickable {
                            navController.navigate("calculadora_agua")
                        },
                        contentScale = ContentScale.Fit
                    )
                }
                //botella y panel indicador
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hidratacion),
                        contentDescription = "Botella de agua",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF448AFF), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = String.format(
                                    Locale.US,
                                    "Llevas:\n%.1f / %.1f L",
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