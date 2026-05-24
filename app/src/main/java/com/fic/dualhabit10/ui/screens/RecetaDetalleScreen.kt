package com.fic.dualhabit10.ui.screens

import android.R.attr.data
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.common.collect.Multimaps.index

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaDetalleScreen(
    recetaId: Int,
    navController: NavController,
    viewModel: AlimentacionViewModel = viewModel()
) {
    val receta = viewModel.buscarRecetaPorId(recetaId)

    receta?.let {data ->
        val colorFondo = Color(android.graphics.Color.parseColor(data.colorHex))

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("preparacion", fontWeight = FontWeight.Bold)},
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
                    model = data.imagenUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(data.nombre, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(data.calorias, fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

                    Spacer (modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer ( modifier = Modifier.height(16.dp))

                    Text("Ingredientes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    data.ingredientes.forEach { ingrediente ->
                        Text ("* $ingrediente", fontSize = 15.sp, modifier = Modifier.padding(vertical = 2.dp), color = Color.DarkGray)
                    }

                    Spacer ( modifier = Modifier.height(20.dp))

                    Text("Pasos a seguir", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    data.pasos.forEachIndexed {index, paso ->
                        Text ("${index + 1}. $paso", fontSize = 15.sp, modifier = Modifier.padding(vertical = 4.dp), color = Color.DarkGray)
                    }
                }
            }
        }
    }
}