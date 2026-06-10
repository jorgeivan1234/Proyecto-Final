package com.fic.dualhabit10.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulCielo
import com.fic.dualhabit10.ui.theme.VerdeDual
import com.fic.dualhabit10.ui.theme.TextoNegro

@Composable
fun InicioScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
    }

    // Validación automática si ya estaba logueado
    LaunchedEffect(Unit) {
        val recordarActivo = sharedPreferences.getBoolean("remember_active", false)
        val sesionIniciada = sharedPreferences.getBoolean("is_logged_in", false)
        if (recordarActivo && sesionIniciada) {
            navController.navigate("habitos") {
                popUpTo("inicio") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulCielo)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.bg_inicio_cielo),
            contentDescription = stringResource(R.string.desc_fondo_cielo_inicio),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Título principal
        Text(
            text = stringResource(R.string.bienvenido_a_dualhabit),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextoNegro,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = Dimens.paddingExtraLarge * 2)
        )

        // Contenedor para alinear el botón de Iniciar
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = Dimens.paddingExtraLarge * 4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeDual
                ),
                modifier = Modifier.size(
                    width = Dimens.buttonWidth,
                    height = Dimens.buttonHeightLarge
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = stringResource(R.string.iniciar),
                    color = TextoNegro,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // Personaje principal
        Image(
            painter = painterResource(id = R.drawable.img_personaje_saludo),
            contentDescription = stringResource(R.string.desc_personaje_saludo),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 70.dp)
                .size(width = Dimens.imageHeroWidth, height = Dimens.imageHeroHeight)
        )

        // Perro IMG
        Image(
            painter = painterResource(id = R.drawable.img_perro_inicio),
            contentDescription = stringResource(R.string.desc_perro_inicio),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-96).dp, y = 50.dp)
                .scale(scaleX = -1f, scaleY = 1f)
                .size(Dimens.imageDogSize)
        )

        // Globo de bienvenida
        Image(
            painter = painterResource(id = R.drawable.img_globo_welcome),
            contentDescription = stringResource(R.string.desc_globo_bienvenida),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 90.dp, y = (-260).dp)
        )
    }
}