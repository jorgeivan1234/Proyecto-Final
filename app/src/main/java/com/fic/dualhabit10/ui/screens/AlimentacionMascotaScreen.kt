package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.data.local.AlimentacionMascotaEntity
import com.fic.dualhabit10.ui.viewmodels.AlimentacionMascotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentacionMascotaScreen(
    navController: NavController,
    viewModel: AlimentacionMascotaViewModel = viewModel()
) {
    val recetas by viewModel.recetas.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF200), shape = RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        Text("Dieta de Mascotas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF7A22))
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = recetas) { receta: AlimentacionMascotaEntity ->
                val colorString = if (receta.colorHex.startsWith("#")) receta.colorHex else "#${receta.colorHex}"
                val colorFondoCard = try {
                    Color(android.graphics.Color.parseColor(colorString))
                } catch (_: Exception) {
                    Color(0xFFFFF9C4)
                }

                // Buscamos si la imagen es un recurso local (drawable)
                val imageResId = context.resources.getIdentifier(
                    receta.imagenUrl,
                    "drawable",
                    context.packageName
                )
                val modeloImagen = if (imageResId != 0) imageResId else receta.imagenUrl

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("receta_mascota_detalle/${receta.id}")
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = colorFondoCard),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = modeloImagen,
                            contentDescription = receta.nombre,
                            modifier = Modifier
                                .size(85.dp)
                                .background(Color.White, shape = RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            SuggestionChip(
                                onClick = { },
                                label = {
                                    val textoMascota = receta.TipoMascota.ifEmpty { "Mascota" }
                                    Text(
                                        text = textoMascota,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                },
                                shape = RoundedCornerShape(50.dp)
                            )
                            Text(
                                receta.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Text(
                                receta.calorias,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                receta.descripcion,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                maxLines = 2,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
