package com.fic.dualhabit10.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Definición de bordes personalizados
val AppShapes = Shapes(
    // Botón de InicioScreen
    extraLarge = RoundedCornerShape(50.dp),

    // Tarjetas de RegisterScreen
    large = RoundedCornerShape(48.dp),
    medium = RoundedCornerShape(24.dp),
    small = RoundedCornerShape(8.dp)
)