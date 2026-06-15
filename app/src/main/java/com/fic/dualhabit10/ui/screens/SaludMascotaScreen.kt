package com.fic.dualhabit10.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.fic.dualhabit10.R
import com.fic.dualhabit10.data.local.SaludMascotaEntity
import com.fic.dualhabit10.ui.viewmodels.SaludMascotaViewModel
import com.fic.dualhabit10.ui.theme.Dimens
import com.fic.dualhabit10.ui.theme.RosaSaludOscuro
import com.fic.dualhabit10.ui.theme.RosaSaludMedio
import com.fic.dualhabit10.ui.theme.RosaSaludClaro
import com.fic.dualhabit10.ui.theme.AzulMedianoche
import com.fic.dualhabit10.ui.theme.GrisPlata
import com.fic.dualhabit10.ui.theme.GrisOscuro
import com.fic.dualhabit10.ui.theme.BlancoFondo
import com.fic.dualhabit10.ui.theme.TextoNegro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaludMascotaScreen(
    navController: NavController,
    viewModel: SaludMascotaViewModel
) {
    val context = LocalContext.current
    val historial by viewModel.historialSalud.collectAsState()

    var selected by remember { mutableStateOf(0) }
    var mostrarFormulario by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(R.string.tab_vacunas),
        stringResource(R.string.tab_tratamientos),
        stringResource(R.string.tab_historial)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_salud_vacunas),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.desc_volver),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RosaSaludMedio
                )
            )
        },
        floatingActionButton = {
            if (selected == 2) {
                FloatingActionButton(
                    onClick = { mostrarFormulario = !mostrarFormulario },
                    containerColor = RosaSaludOscuro,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.btn_add_registro)
                    )
                }
            } else {
                ExtendedFloatingActionButton(
                    onClick = {
                        val gmmIntentUri = Uri.parse("geo:0,0?q=veterinaria+cerca+de+mi")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        context.startActivity(mapIntent)
                    },
                    containerColor = RosaSaludOscuro,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(Dimens.paddingSmall))
                    Text(stringResource(R.string.btn_buscar_vet))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RosaSaludClaro)
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selected,
                containerColor = RosaSaludMedio.copy(alpha = 0.2f),
                contentColor = RosaSaludOscuro
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selected == index,
                        onClick = { selected = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = Dimens.textSizeSmall
                            )
                        }
                    )
                }
            }

            when (selected) {
                0 -> InfoVacunasSeccion()
                1 -> InfoTratamientosSeccion()
                2 -> HistorialCompletoSeccion(
                    historial = historial,
                    mostrarFormulario = mostrarFormulario,
                    viewModel = viewModel,
                    onCerrarFormulario = { mostrarFormulario = false }
                )
            }
        }
    }
}

@Composable
fun InfoVacunasSeccion() {
    val vacunasRecomendadas = listOf(
        stringResource(R.string.vacuna_rabia_titulo) to stringResource(R.string.vacuna_rabia_desc),
        stringResource(R.string.vacuna_quintuple_titulo) to stringResource(R.string.vacuna_quintuple_desc),
        stringResource(R.string.vacuna_giardiasis_titulo) to stringResource(R.string.vacuna_giardiasis_desc)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingDefault),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
    ) {
        item {
            Text(
                text = stringResource(R.string.vacunas_guia_titulo),
                fontWeight = FontWeight.Bold,
                fontSize = Dimens.textSizeBodyLarge,
                color = AzulMedianoche
            )
            Text(
                text = stringResource(R.string.vacunas_guia_desc),
                fontSize = Dimens.textSizeMedium,
                color = GrisOscuro
            )
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
        }

        items(vacunasRecomendadas) { (titulo, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        Dimens.dividerThickness,
                        RosaSaludMedio.copy(alpha = 0.3f),
                        RoundedCornerShape(Dimens.cornerRadiusSmall)
                    ),
                colors = CardDefaults.cardColors(containerColor = BlancoFondo)
            ) {
                Column(modifier = Modifier.padding(Dimens.paddingDefault)) {
                    Text(
                        text = titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.textSizeBody,
                        color = RosaSaludOscuro
                    )
                    Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                    Text(
                        text = desc,
                        fontSize = Dimens.textSizeSmall,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun InfoTratamientosSeccion() {
    val tipsSalud = listOf(
        stringResource(R.string.tratamiento_desparasitacion_titulo) to stringResource(R.string.tratamiento_desparasitacion_desc),
        stringResource(R.string.tratamiento_antipulgas_titulo) to stringResource(R.string.tratamiento_antipulgas_desc),
        stringResource(R.string.tratamiento_med_titulo) to stringResource(R.string.tratamiento_med_desc)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingDefault),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
    ) {
        item {
            Text(
                text = stringResource(R.string.tratamientos_titulo),
                fontWeight = FontWeight.Bold,
                fontSize = Dimens.textSizeBodyLarge,
                color = AzulMedianoche
            )
            Text(
                text = stringResource(R.string.tratamientos_desc),
                fontSize = Dimens.textSizeMedium,
                color = GrisOscuro
            )
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
        }

        items(tipsSalud) { (titulo, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        Dimens.dividerThickness,
                        RosaSaludMedio.copy(alpha = 0.3f),
                        RoundedCornerShape(Dimens.cornerRadiusSmall)
                    ),
                colors = CardDefaults.cardColors(containerColor = BlancoFondo)
            ) {
                Column(modifier = Modifier.padding(Dimens.paddingDefault)) {
                    Text(
                        text = titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.textSizeBody,
                        color = RosaSaludOscuro
                    )
                    Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                    Text(
                        text = desc,
                        fontSize = Dimens.textSizeSmall,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun HistorialCompletoSeccion(
    historial: List<SaludMascotaEntity>,
    mostrarFormulario: Boolean,
    viewModel: SaludMascotaViewModel,
    onCerrarFormulario: () -> Unit
) {
    val tiposRegistros = listOf(
        stringResource(R.string.chip_vacuna),
        stringResource(R.string.chip_desparasitante),
        stringResource(R.string.chip_nota)
    )

    Column(modifier = Modifier.fillMaxSize().padding(Dimens.paddingDefault)) {
        AnimatedVisibility(visible = mostrarFormulario) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.paddingDefault)
                    .border(Dimens.paddingMicro, RosaSaludOscuro, RoundedCornerShape(Dimens.cornerRadiusDefault)),
                colors = CardDefaults.cardColors(containerColor = BlancoFondo)
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.paddingDefault),
                    verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
                ) {
                    Text(
                        text = stringResource(R.string.form_nuevo_registro),
                        fontWeight = FontWeight.Bold,
                        color = RosaSaludOscuro
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
                    ) {
                        tiposRegistros.forEach { tipo ->
                            val seleccionado = viewModel.tipoRegistro == tipo
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(Dimens.paddingSmall))
                                    .background(if (seleccionado) RosaSaludOscuro else GrisPlata)
                                    .clickable { viewModel.tipoRegistro = tipo }
                                    .padding(vertical = Dimens.paddingSmall),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tipo,
                                    color = if (seleccionado) Color.White else TextoNegro,
                                    fontSize = Dimens.textSizeExtraSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = viewModel.nombreRegistro,
                        onValueChange = {
                            if (it.length <= 40) viewModel.nombreRegistro = it
                        },
                        label = { Text(stringResource(R.string.hint_nombre_registro)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = viewModel.fechaRegistro,
                        onValueChange = { input ->
                            // Ahora SOLAMENTE guardamos los números limpios, máximo 8 dígitos
                            val digitos = input.filter { it.isDigit() }.take(8)
                            viewModel.fechaRegistro = digitos
                        },
                        label = { Text(stringResource(R.string.hint_fecha_registro)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        // Usamos la transformación visual para que se "dibujen" las / sin afectar el cursor
                        visualTransformation = DateTransformation()
                    )

                    OutlinedTextField(
                        value = viewModel.notasRegistro,
                        onValueChange = {
                            if (it.length <= 100) viewModel.notasRegistro = it
                        },
                        label = { Text(stringResource(R.string.hint_notas_registro)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    Button(
                        onClick = { viewModel.agregarRegistro { onCerrarFormulario() } },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = RosaSaludOscuro)
                    ) {
                        Text(
                            text = stringResource(R.string.btn_guardar_bitacora),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (historial.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.empty_state_historial),
                    color = GrisOscuro,
                    fontSize = Dimens.textSizeMedium
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)) {
                items(historial) { registro ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = BlancoFondo)
                    ) {
                        Row(
                            modifier = Modifier.padding(Dimens.paddingDefault),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(Dimens.paddingSmallMedium))
                                            .background(RosaSaludMedio.copy(alpha = 0.2f))
                                            .padding(
                                                horizontal = Dimens.paddingSmallMedium,
                                                vertical = Dimens.paddingMicro
                                            )
                                    ) {
                                        Text(
                                            text = registro.tipo,
                                            color = RosaSaludOscuro,
                                            fontSize = Dimens.textSizeMicro,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(Dimens.paddingSmall))

                                    // Formateamos la fecha para la vista en caso de que esté guardada solo como números
                                    val digitosFecha = registro.fecha.filter { it.isDigit() }
                                    val fechaMostrar = if (digitosFecha.length == 8) {
                                        "${digitosFecha.substring(0, 2)}/${digitosFecha.substring(2, 4)}/${digitosFecha.substring(4, 8)}"
                                    } else {
                                        registro.fecha // Por si hay registros antiguos guardados con otro formato
                                    }

                                    Text(
                                        text = fechaMostrar,
                                        fontSize = Dimens.textSizeExtraSmall,
                                        color = GrisOscuro
                                    )
                                }
                                Spacer(modifier = Modifier.height(Dimens.paddingTiny))
                                Text(
                                    text = registro.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = Dimens.textSizeBody,
                                    color = AzulMedianoche
                                )
                                if(registro.notas.isNotBlank()) {
                                    Text(
                                        text = registro.notas,
                                        fontSize = Dimens.textSizeSmall,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                            IconButton(onClick = { viewModel.eliminarRegistro(registro) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.desc_eliminar),
                                    tint = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Transformación visual para fechas dd/mm/aaaa
class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Obtenemos solo los números
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 || i == 3) out += "/"
        }

        // Mapeamos las posiciones del cursor para que no salte
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}