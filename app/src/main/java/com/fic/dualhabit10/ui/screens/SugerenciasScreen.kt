package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.SugerenciasViewModel
import kotlinx.coroutines.launch
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.VerdeFondoHabitos
import com.fic.dualhabit10.ui.theme.NaranjaCabecera
import com.fic.dualhabit10.ui.theme.AmarilloFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.AzulOscuroSueno

@Composable
fun Sugerencias(
    navController: NavHostController,
    viewModel: SugerenciasViewModel
) {
    var sugerenciaTexto by remember { mutableStateOf("") }
    val limiteCaracteres = 500

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scoper = rememberCoroutineScope()

    BaseCustomDrawer(navController = navController, drawerState = drawerState) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondoHabitos)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = NaranjaCabecera,
                        shape = RoundedCornerShape(bottomStart = Dimens.cornerRadiusMedium, bottomEnd = Dimens.cornerRadiusMedium)
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
                        onClick = { scoper.launch { drawerState.open() } },
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
                            .padding(horizontal = Dimens.spacerMedium, vertical = Dimens.paddingSmallMedium)
                    ) {
                        Text(
                            text = stringResource(R.string.title_sugerencias),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextoNegro
                        )
                    }
                }
            }

            // Formulario de Sugerencias
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.title_buzon_sugerencias),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Dimens.paddingDefault),
                    color = TextoNegro
                )

                TextField(
                    value = sugerenciaTexto,
                    onValueChange = { nuevoTexto ->
                        if (nuevoTexto.length <= limiteCaracteres) {
                            sugerenciaTexto = nuevoTexto
                        }
                    },
                    label = { Text(stringResource(R.string.hint_escribe_comentario)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.textAreaHeight),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(Dimens.paddingSmall))

                Text(
                    text = "${sugerenciaTexto.length} / $limiteCaracteres",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End),
                    color = GrisOscuro
                )

                Spacer(modifier = Modifier.height(Dimens.paddingDefault))

                Button(
                    onClick = {
                        if (sugerenciaTexto.isNotBlank()) {
                            val textoLimpio = sugerenciaTexto.trim()
                            viewModel.guardarSugerencias(textoLimpio)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulOscuroSueno,
                        contentColor = TextoBlanco
                    )
                ) {
                    Text(stringResource(R.string.btn_enviar_sugerencia), fontSize = 16.sp)
                }
            }
        }
    }
}