package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.ui.viewmodels.AlimentacionMascotaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentacionMascotaDetalleScreen(
    recetaId: Int,
    navController: NavController,
    viewModel: AlimentacionMascotaViewModel = viewModel()
) {
    //busca la actividad por ID
    val receta by viewModel.buscarRecetaPorId(recetaId).collectAsState(initial = null)
    val context = LocalContext.current

    receta?.let { data ->
        val colorString = if (data.colorHex.startsWith("#")) data.colorHex else "#${data.colorHex}"
        val colorBarra = try {
            Color(android.graphics.Color.parseColor(colorString))
        } catch (e: Exception) {
            Color(0xFFFF7A22)
        }

        val colorFondoPantalla = Color(0xFFBFF7E8)
        val imageResId = context.resources.getIdentifier(
            data.imagenUrl,
            "drawable",
            context.packageName
        )
        val modeloImagen = if (imageResId != 0) imageResId else data.imagenUrl

        Scaffold(
            containerColor = colorFondoPantalla,
            topBar = {
                TopAppBar(
                    title = { Text("Detalle de Rutina", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = colorBarra)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorFondoPantalla)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // imagen real
                AsyncImage(
                    model = modeloImagen,
                    contentDescription = data.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = data.nombre,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text(text = "Apto para: ${data.TipoMascota}", color = Color.Black) },
                        )
                        SuggestionChip(
                            onClick = { },
                            label = { Text(text = data.calorias, color = Color.Black) },
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.12f))
                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "Descripcion de la Dieta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = data.descripcion,
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    //tarjeta de sugerencia
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorBarra.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "¡Asegurate de consultar con tu veterinario las porciones ideales deacuerdo al peso y raza de tu mascota!",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}