package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.ui.viewmodels.HigieneMascotaViewModel
import java.time.LocalDate
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.VerdeCheck
import com.fic.dualhabit10.ui.theme.RosaDestacado
import com.fic.dualhabit10.ui.theme.AzulCard
import com.fic.dualhabit10.ui.theme.AzulHidratacion
import com.fic.dualhabit10.ui.theme.AzulObscuro
import com.fic.dualhabit10.ui.theme.BlancoFondo
import com.fic.dualhabit10.ui.theme.TextoNegro
import com.fic.dualhabit10.ui.theme.GrisOscuro

// Pantalla principal de gestión de higiene para la mascota que monitoriza el cumplimiento de tareas diarias
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigieneMascotaScreen(
    navController: NavController,
    viewModel: HigieneMascotaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val estadoHigiene by viewModel.estadoHigiene.collectAsState()

    // Sincronización inicial del estado diario al cargar el componente basándose en la fecha actual del sistema
    LaunchedEffect(Unit) {
        viewModel.cargarDatosDiarios(LocalDate.now().toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_higiene_mascota),
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
                text = stringResource(R.string.title_tareas_diarias),
                fontSize = Dimens.textSizeSubtitle,
                fontWeight = FontWeight.Bold,
                color = TextoNegro,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = Dimens.paddingDefault)
            )

            // Tarjetas de hábitos diarios
            ItemHigieneDiaria(
                titulo = stringResource(R.string.tarea_dental_titulo),
                descripcion = stringResource(R.string.tarea_dental_desc),
                icono = Icons.Default.Pets,
                checkColor = AzulObscuro,
                isChecked = estadoHigiene.higieneDental,
                onCheckedChange = { viewModel.actualizarHabitoDiario("dental", it) }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Tarjetas de hábitos diarios
            ItemHigieneDiaria(
                titulo = stringResource(R.string.tarea_cepillado_titulo),
                descripcion = stringResource(R.string.tarea_cepillado_desc),
                icono = Icons.Default.CrueltyFree,
                checkColor = VerdeCheck,
                isChecked = estadoHigiene.cepilladoPelo,
                onCheckedChange = { viewModel.actualizarHabitoDiario("cepillado", it) }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            // Tarjetas de hábitos diarios
            ItemHigieneDiaria(
                titulo = stringResource(R.string.tarea_entorno_titulo),
                descripcion = stringResource(R.string.tarea_entorno_desc),
                icono = Icons.Default.CleanHands,
                checkColor = RosaDestacado,
                isChecked = estadoHigiene.limpiezaEntorno,
                onCheckedChange = { viewModel.actualizarHabitoDiario("entorno", it) }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingXXLarge))

            // Tarjeta de acceso al panel de control cronológico extendido (baño, corte de uñas)
            Card(
                onClick = { navController.navigate("higiene_mascota_detalle") },
                shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
                colors = CardDefaults.cardColors(
                    containerColor = AzulCard
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.heightCardControl)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimens.paddingDefault),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPasteSearch,
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.iconSizeCard),
                        tint = TextoNegro
                    )
                    Spacer(modifier = Modifier.width(Dimens.paddingDefault))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.card_control_titulo),
                            fontSize = Dimens.textSizeBodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextoNegro
                        )
                        Text(
                            text = stringResource(R.string.card_control_desc),
                            fontSize = Dimens.textSizeMedium,
                            color = GrisOscuro
                        )
                    }
                }
            }
        }
    }
}

// Componente modular interactivo tipo tarjeta para representar una tarea de higiene diaria
@Composable
fun ItemHigieneDiaria(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    checkColor: Color,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = BlancoFondo),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingDefault),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.iconSizeBox)
                    .background(
                        color = checkColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(Dimens.cornerRadiusMediumLarge)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = checkColor
                )
            }
            Spacer(modifier = Modifier.width(Dimens.paddingDefault))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = Dimens.textSizeBody,
                    fontWeight = FontWeight.Bold,
                    color = TextoNegro
                )
                Text(
                    text = descripcion,
                    fontSize = Dimens.textSizeExtraSmall,
                    color = GrisOscuro
                )
            }
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = checkColor
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHigieneMascotaScreen() {
    val nav = rememberNavController()
    HigieneMascotaScreen(navController = nav)
}