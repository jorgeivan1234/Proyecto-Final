package com.fic.dualhabit10.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulCielo
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisTextoHint
import com.fic.dualhabit10.ui.theme.TextoBlanco

// Pantalla informativa para secciones en desarrollo o bajo mantenimiento administrativo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantenimientoScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BaseCustomDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AzulCielo)
        ) {
            // Contenedor superior para la barra de navegación personalizada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.topBarHeightExtra)
                    .background(
                        color = NaranjaCabecera,
                        shape = RoundedCornerShape(
                            bottomStart = Dimens.cornerRadiusExtraLarge,
                            bottomEnd = Dimens.cornerRadiusExtraLarge
                        )
                    )
                    .padding(horizontal = Dimens.paddingMedium)
                    .padding(top = Dimens.paddingLarge),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.desc_menu),
                            tint = TextoNegro,
                            modifier = Modifier.size(Dimens.iconSizeLarge)
                        )
                    }

                    Text(
                        text = stringResource(R.string.title_proximamente),
                        color = TextoNegro,
                        fontSize = Dimens.textSizeBodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Margen de compensación para equilibrar la alineación del título
                    Spacer(modifier = Modifier.size(Dimens.iconSizeLarge))
                }
            }

            // Organización visual de los elementos centrales de la interfaz
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconSizeGiant),
                    tint = NaranjaCabecera
                )

                Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))

                Text(
                    text = stringResource(R.string.text_en_construccion),
                    fontSize = Dimens.textSizeSubtitle,
                    fontWeight = FontWeight.Bold,
                    color = TextoNegro,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Dimens.paddingDefault))

                Text(
                    text = stringResource(R.string.text_mantenimiento_desc),
                    fontSize = Dimens.textSizeBody,
                    color = GrisTextoHint,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = Dimens.paddingDefault)
                )

                Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))

                // Control de navegación para retornar al estado previo en la pila
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = NaranjaCabecera),
                    shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.buttonHeight)
                ) {
                    Text(
                        text = stringResource(R.string.btn_regresar_inicio),
                        fontSize = Dimens.textSizeBody,
                        fontWeight = FontWeight.Bold,
                        color = TextoBlanco
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMantenimientoScreen() {
    val nav = rememberNavController()
    MantenimientoScreen(navController = nav)
}