package com.fic.dualhabit10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.BoxScope
import com.fic.dualhabit10.R

@Composable
fun Menu_Mascotas(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5EDCFF))
    ){
    Column(
        modifier = Modifier
            .width(360.dp)
            .align(Alignment.TopCenter)
            .background(
                color = Color(0xD9D9D9),
                shape = RoundedCornerShape(48.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_perro_coloreado),
            contentDescription = null,
            modifier = Modifier.size(280.dp)
        )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMenu() {
    val nav = rememberNavController()
    Menu_Mascotas(navController = nav)
}