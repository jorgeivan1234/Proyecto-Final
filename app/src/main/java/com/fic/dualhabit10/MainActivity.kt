package com.fic.dualhabit10

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.view.WindowCompat
import com.fic.dualhabit10.ui.screens.Forget_Password
import com.fic.dualhabit10.ui.screens.HabitosScreen
import com.fic.dualhabit10.ui.screens.InicioScreen
import com.fic.dualhabit10.ui.screens.LoginScreen
import com.fic.dualhabit10.ui.screens.Mascotas_V
import com.fic.dualhabit10.ui.screens.RegisterScreen
import com.fic.dualhabit10.ui.screens.RegisterSuccessful


class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent{
            MaterialTheme{
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "inicio"
                ) {
                    composable("inicio"){
                        InicioScreen(navController = navController)
                    }
                    composable("login"){
                        LoginScreen(navController = navController)
                    }
                    composable("forget_password"){
                        Forget_Password(navController = navController)
                    }
                    composable("register"){
                        RegisterScreen(navController = navController)
                    }
                    composable("register_successful"){
                        RegisterSuccessful(navController = navController)
                    }
                    composable("habitos") {
                        HabitosScreen(navController = navController)
                    }
                }
            }
        }
    }
}