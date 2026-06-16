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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HigieneMascotaViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.AzulHidratacion
import com.fic.dualhabit10.ui.theme.AzulFuerte
import com.fic.dualhabit10.ui.theme.BlancoFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.VerdeCheck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigieneMascotaDetalleScreen(
    navController: NavController,
    viewModel: HigieneMascotaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val diasBano by viewModel.diasDesdeBano.collectAsState()
    val diasUnas by viewModel.diasDesdeUnas.collectAsState()

    // Recargar datos para procesar contadores
    LaunchedEffect(Unit) {
        viewModel.cargarDatosDiarios()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_fechas_historial),
                        fontWeight = FontWeight.Bold,
                        color = TextoNegro
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_volver),
                            tint = TextoNegro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulHidratacion
                )
            )
        },
        containerColor = AzulHidratacion
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.paddingDefault),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.title_cronograma_cuidados),
                fontSize = Dimens.textSizeLarge,
                fontWeight = FontWeight.Bold,
                color = TextoNegro,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = Dimens.paddingMediumLarge)
            )

            // Alerta de sección: baño límite de 15 días
            TarjetaAlertaPeriodica(
                titulo = stringResource(R.string.alerta_ultimo_bano),
                diasTranscurridos = diasBano,
                limiteDias = 15,
                onRegistrarHoy = { viewModel.registrarBano() }
            )

            Spacer(modifier = Modifier.height(Dimens.spacerMedium))

            // Alerta de sección: uñas límite de 30 días
            TarjetaAlertaPeriodica(
                titulo = stringResource(R.string.alerta_ultimo_corte),
                diasTranscurridos = diasUnas,
                limiteDias = 30,
                onRegistrarHoy = { viewModel.registrarCorteUnas() }
            )
        }
    }
}

@Composable
fun TarjetaAlertaPeriodica(
    titulo: String,
    diasTranscurridos: Long?,
    limiteDias: Int,
    onRegistrarHoy: () -> Unit
) {
    // Determinar el color del estado según los días pasados
    val colorEstado = when {
        diasTranscurridos == null -> GrisOscuro
        diasTranscurridos >= limiteDias -> colorResource(id = R.color.rojo)
        diasTranscurridos >= (limiteDias - 4) -> Color(0xFFFFB300) // Naranja alerta
        else -> VerdeCheck
    }

    // Traducción dinámica con parámetros
    val textoDias = when (diasTranscurridos) {
        null -> stringResource(R.string.estado_sin_registros)
        0L -> stringResource(R.string.estado_hoy)
        1L -> stringResource(R.string.estado_ayer)
        else -> stringResource(R.string.estado_dias, diasTranscurridos)
    }

    Card(
        shape = RoundedCornerShape(Dimens.cornerRadiusXXLarge),
        colors = CardDefaults.cardColors(containerColor = BlancoFondo),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingMediumLarge)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = titulo,
                    fontSize = Dimens.textSizeBodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextoNegro
                )
                Box(
                    modifier = Modifier
                        .size(Dimens.indicatorSizeSmall)
                        .background(colorEstado, RoundedCornerShape(Dimens.cornerRadiusLarge))
                )
            }
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))

            Text(
                text = textoDias,
                fontSize = Dimens.textSizeTitle,
                fontWeight = FontWeight.ExtraBold,
                color = colorEstado
            )

            Text(
                // Inyectamos el límite de días directo al string
                text = stringResource(R.string.intervalo_recomendado, limiteDias),
                fontSize = Dimens.textSizeExtraSmall,
                color = GrisOscuro,
                modifier = Modifier.padding(top = Dimens.paddingMicro)
            )
            Spacer(modifier = Modifier.height(Dimens.paddingDefault))

            // Botón interactivo para registrar una acción
            Button(
                onClick = onRegistrarHoy,
                colors = ButtonDefaults.buttonColors(containerColor = AzulFuerte),
                shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconSizeButton)
                )
                Spacer(modifier = Modifier.width(Dimens.paddingSmall))
                Text(
                    text = stringResource(R.string.btn_hecho_hoy),
                    fontSize = Dimens.textSizeMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}