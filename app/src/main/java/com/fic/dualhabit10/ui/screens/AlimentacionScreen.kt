package com.fic.dualhabit10.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.fic.dualhabit10.ui.viewmodels.AlimentacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentacionScreen(
    navController: NavController,
    viewModel: AlimentacionViewModel = viewModel()
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
                        Text( "Recetas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton( onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFFF7A22))
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { abrirMapaRestaurantesSalusables(context) },
                containerColor = Color(0xFFFF7A22),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Mapa")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Restaurantes saludables cercanos", fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF9EFFEB))
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = recetas) { receta ->
                val colorFondoCard = Color(android.graphics.Color.parseColor(receta.colorHex))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = colorFondoCard),
                    onClick = { navController.navigate("receta_detalle/${receta.id}")}
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = receta.imagenUrl,
                            contentDescription = receta.nombre,
                            modifier = Modifier
                                .size(85.dp)
                                .background(Color.White, shape = RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(receta.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                            Text(receta.calorias, fontSize = 14.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(receta.descripcion, fontSize = 12.sp, lineHeight = 16.sp, maxLines = 2, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

fun abrirMapaRestaurantesSalusables(context: Context) {
    val intentUri = Uri.parse("geo:0,0?q=restaurantes+salusables+comida+nutrituva")
    val mapIntent = Intent(Intent.ACTION_VIEW, intentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if(mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/q=restaurantes+saludables"))
        context.startActivity(browserIntent)
    }
}