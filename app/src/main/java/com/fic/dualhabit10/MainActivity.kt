package com.fic.dualhabit10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.view.WindowCompat
import com.fic.dualhabit10.ui.screens.Forget_Password
import com.fic.dualhabit10.ui.screens.HabitosScreen
import com.fic.dualhabit10.ui.screens.HidratacionScreen
import com.fic.dualhabit10.ui.screens.InicioScreen
import com.fic.dualhabit10.ui.screens.LoginScreen
import com.fic.dualhabit10.ui.screens.MascotasMenu
import com.fic.dualhabit10.ui.screens.PerfilScreen
import com.fic.dualhabit10.ui.screens.RegisterScreen
import com.fic.dualhabit10.ui.screens.RegisterSuccessful


class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent{
            MaterialTheme{
                val navController = rememberNavController()

                /*
                    Cualquier Modificación al NavHost debe añadir de donde sale hacia donde redirecciona
                    ejemplo

                    //InicioScreen -> LoginScreen
                    composable("inicio"){   //Donde inicia la acción
                        InicioScreen(navController = navController) //Donde redirige la acción
                    }
                */
                NavHost(
                    navController = navController,
                    startDestination = "inicio"
                ) {
                    //InicioScreen -> LoginScreen
                    composable("inicio"){
                        InicioScreen(navController = navController)
                    }
                    //LoginScreen -> HabitosScreen
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    //LoginScreen -> ForgetPassword
                    composable("forget_password"){
                        Forget_Password(navController = navController)
                    }
                    //LoginScreen -> RegisterScreen
                    composable("register"){
                        RegisterScreen(navController = navController)
                    }
                    //RegisterScreen -> RegisterSuccessful
                    composable("register_successful"){
                        RegisterSuccessful(navController = navController)
                    }
                    //LoginScreen -> HabitosScreen
                    composable("habitos") {
                        HabitosScreen(navController = navController)
                    }
                    //HabitosScreen -> MascotasMenu
                    composable("mascota_menu"){
                        MascotasMenu(navController = navController)
                    }
                    //HabitosScreen -> HidratacionScreen
                    composable(route = "hidratacion"){
                        HidratacionScreen(navController = navController)
                    }
                    //tarjeta -> PerfilScreen
                    composable("perfil") {
                        PerfilScreen(navController = navController)
                    }
                }
            }
        }
    }
}