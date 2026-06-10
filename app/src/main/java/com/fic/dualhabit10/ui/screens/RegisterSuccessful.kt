package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulCielo
import com.fic.dualhabit10.ui.theme.AzulBase
import com.fic.dualhabit10.ui.theme.AzulFuerte
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.TextoBlanco

@Composable
fun RegisterSuccessful(navController: NavHostController) {

    // Contenedor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulCielo)
    ) {
        // Imagen de fondo (cielo/nubes) que decora la pantalla completa
        Image(
            painter = painterResource(id = R.drawable.bg_inicio_cielo),
            contentDescription = stringResource(R.string.desc_fondo_cielo),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Tarjeta central flotante donde mostramos el mensaje de éxito
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(Dimens.cardContainerWidth)
                .background(
                    color = AzulBase,
                    shape = MaterialTheme.shapes.large
                )
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal
            Text(
                text = stringResource(id = R.string.title_registro_exitoso),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextoNegro
            )

            Spacer(modifier = Modifier.height(Dimens.spacerExtraLarge))

            // Imagen ilustrativa
            Image(
                painter = painterResource(id = R.drawable.img_perro_saltando),
                contentDescription = stringResource(R.string.desc_perro_saltando),
                modifier = Modifier.size(Dimens.imageDogLarge), // Nuevo valor (280.dp)
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(Dimens.paddingSmall))

            // Botón final para avanzar a la pantalla de inicio de sesión
            Button(
                onClick = {
                    // Una vez que el usuario lee el mensaje, lo mandamos al login
                    navController.navigate("login") {
                        // Limpiamos el backstack para que no puedan "volver" al registro exitoso
                        popUpTo("register_successful") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulFuerte
                ),
                modifier = Modifier.size(width = Dimens.buttonWidth, height = Dimens.buttonHeight),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = stringResource(id = R.string.btn_continuar),
                    color = TextoBlanco,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}