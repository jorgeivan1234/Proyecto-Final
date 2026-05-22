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
import java.util.Locale
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch

@Composable
fun HidratacionScreen(navController: NavController,
                      viewModel: HidratacionViewModel = viewModel()
 ) {
    //conversion directa a litros para cada caja
    val litrosConsumidos = viewModel.aguaConsumidaML / 1000f
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
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir Menu",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = "Hidratacion",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF81D4FA), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .align(Alignment.CenterEnd)
                            .clickable {/* ruta futra*/ }
                    ) {
                        Text(
                            text = "Historial",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.img_hidratacion),
                    contentDescription = "Cuenco de agua",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // contenedor principal
                Column(
                    modifier = Modifier.weight(1.3f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    //botones
                    BotonConsumoOvalado(texto = "+250ml", onClick = { viewModel.agregarAgua(250) })
                    Spacer(modifier = Modifier.height(12.dp))
                    BotonConsumoOvalado(texto = "+500ml", onClick = { viewModel.agregarAgua(500) })
                    Spacer(modifier = Modifier.height(12.dp))
                    BotonConsumoOvalado(texto = "+1L", onClick = { viewModel.agregarAgua(1000) })

                    Spacer(modifier = Modifier.height(40.dp))
                    //texto informativo
                    Text(
                        text = "Calcul\ncuanta debes\ntomar",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    //boton de calcular
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFEE58), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 32.dp, vertical = 12.dp)
                            .clickable { navController.navigate("perfil_screen") }
                    ) {
                        Text(
                            text = "Calcular",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                //botella y panel indicador
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hidratacion),
                        contentDescription = "Botella de agua",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF448AFF), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Consumo:\n${
                                String.format(
                                    Locale.US,
                                    "%.1f",
                                    litrosConsumidos
                                )
                            } litros",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
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
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .width(130.dp)
            .background(Color.Black, shape = RoundedCornerShape(50.dp))
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewHidratacion(){
    HidratacionScreen(navController = rememberNavController())
}