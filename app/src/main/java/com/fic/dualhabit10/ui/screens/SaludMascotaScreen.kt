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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fic.dualhabit10.data.local.SaludMascotaEntity
import com.fic.dualhabit10.ui.viewmodels.SaludMascotaViewModel

private object SaludColors {
    val StrawberryDreams = Color(0xFFFF85A2)
    val mistyrose = Color(0xFFFFF0F3)
    val hotPink = Color(0xFFFF477E)
    val midnight = Color(0xFF2B2D42)
    val lightSilver = Color(0xFFE0E0E0)
}

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

    val tabs = listOf("Vacunas", "Tratamientos", "Historial")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Salud y Vacunas",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atras",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SaludColors.StrawberryDreams
                )
            )
        },
        floatingActionButton = {
            if (selected == 2) {
                FloatingActionButton(
                    onClick = { mostrarFormulario = !mostrarFormulario },
                    containerColor = SaludColors.hotPink,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir registro"
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
                    containerColor = SaludColors.hotPink,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buscar Veterinarias")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SaludColors.mistyrose)
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selected,
                containerColor = SaludColors.StrawberryDreams.copy(alpha = 0.2f),
                contentColor = SaludColors.hotPink
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selected == index,
                        onClick = { selected = index },
                        text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
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
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Guia Esencial de Vacunacion", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SaludColors.midnight)
            Text("Asegurate de mantener al dia el calendario de tu mascota.", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
        }
        val vacunasRecomendadas = listOf(
            "Vacuna Antirrábica 🦠" to "Obligatoria por ley. Se aplica por primera vez a los 3-4 meses de edad y requiere un refuerzo anual obligatorio.",
            "Vacuna Quíntuple / Triple Felina 🐕🐈" to "Protege contra enfermedades mortales como el Parvovirus, Moquillo, Hepatitis y Leptospirosis. Refuerzo cada año.",
            "Vacuna contra la Giardiasis 🧼" to "Opcional pero altamente recomendada si tu mascota suele frecuentar parques públicos o convivir con otros animales."
        )
        items(vacunasRecomendadas) { (titulo, desc) ->
            Card(
                modifier = Modifier.fillMaxWidth().border(
                    1.dp,
                    SaludColors.StrawberryDreams.copy(alpha = 0.3f),
                    RoundedCornerShape(12.dp)
                ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SaludColors.hotPink
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(desc, fontSize = 13.sp, color = Color.DarkGray)
                }
            }
        }
    }
}

@Composable
fun InfoTratamientosSeccion() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Control de Parásitos y Medicación", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SaludColors.midnight)
            Text("Información clave para la salud preventiva del día a día.", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
        }
        val tipsSalud = listOf(
            "Desparasitación Interna 💊" to "Se debe realizar de forma ideal cada 3 o 4 meses utilizando la dosis exacta según el peso actual de tu mascota.",
            "Antipulgas y Garrapatas 🛡️" to "Ya sea mediante pipetas mensuales, collares o pastillas masticables. Vital para evitar la transmisión de enfermedades.",
            "Uso Responsable de Medicamentos ⚠️" to "Nunca automediques a tu consentido. El uso incorrecto de dosis puede dañar severamente sus órganos internos."
        )
        items(tipsSalud) { (titulo, desc) ->
            Card(
                modifier = Modifier.fillMaxWidth().border(
                    1.dp,
                    SaludColors.StrawberryDreams.copy(alpha = 0.3f),
                    RoundedCornerShape(12.dp)
                ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SaludColors.hotPink)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(desc, fontSize = 13.sp, color = Color.DarkGray)
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
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AnimatedVisibility(visible = mostrarFormulario) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).border(2.dp, SaludColors.hotPink, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Nuevo Registro Sanitario",
                        fontWeight = FontWeight.Bold,
                        color = SaludColors.hotPink
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Vacuna", "Desparasitante", "Nota").forEach { tipo ->
                            val seleccionado = viewModel.tipoRegistro == tipo
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (seleccionado) SaludColors.hotPink else SaludColors.lightSilver)
                                    .clickable { viewModel.tipoRegistro = tipo }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tipo,
                                    color = if (seleccionado) Color.White else Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = viewModel.nombreRegistro,
                        onValueChange = { viewModel.nombreRegistro = it },
                        label = { Text("Nombre (Ej: Triple Felina, Control Pulgas)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = viewModel.fechaRegistro,
                        onValueChange = { viewModel.fechaRegistro = it },
                        label = { Text("Fecha (DD/MM/AAAA)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    OutlinedTextField(
                        value = viewModel.notasRegistro,
                        onValueChange = { viewModel.notasRegistro = it },
                        label = { Text("Notas u observaciones adicionales") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    Button(
                        onClick = { viewModel.agregarRegistro { onCerrarFormulario() } },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SaludColors.hotPink)
                    ) {
                        Text(
                            "Guardar en Bitacora",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (historial.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay Vacunas ni notas registradas aun.\nUsa el boton '+' para añadir.", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(historial) { registro ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SaludColors.StrawberryDreams.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = registro.tipo,
                                            color = SaludColors.hotPink,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(registro.fecha, fontSize = 12.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(registro.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SaludColors.midnight)
                                if(registro.notas.isNotBlank()) {
                                    Text(registro.notas, fontSize = 13.sp, color = Color.DarkGray)
                                }
                            }
                            IconButton(onClick = { viewModel.eliminarRegistro(registro) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}