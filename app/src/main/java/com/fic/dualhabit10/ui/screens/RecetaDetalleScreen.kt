package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fic.dualhabit10.ui.viewmodels.AlimentacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaDetalleScreen(
    recetaId: Int,
    navController: NavController,
    viewModel: AlimentacionViewModel = viewModel()
) {
    val recetas by viewModel.recetas.collectAsState()

    val receta = recetas.find { it.id == recetaId }

    if (receta != null) {
        val colorFondo = try {
            Color(android.graphics.Color.parseColor(receta.colorHex))
        } catch (e: Exception) {
            Color.LightGray
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Preparación", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = colorFondo)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = receta.imagenUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(receta.nombre, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(receta.calorias, fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Ingredientes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    receta.ingredientes.forEach { ingrediente ->
                        Text("* $ingrediente", fontSize = 15.sp, modifier = Modifier.padding(vertical = 2.dp), color = Color.DarkGray)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Pasos a seguir", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    receta.pasos.forEachIndexed { index, paso ->
                        Text("${index + 1}. $paso", fontSize = 15.sp, modifier = Modifier.padding(vertical = 4.dp), color = Color.DarkGray)
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}