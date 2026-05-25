package com.fic.dualhabit10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fic.dualhabit10.ui.screens.ActividadFisicaScreen
import com.fic.dualhabit10.ui.screens.AlimentacionMascotaScreen
import com.fic.dualhabit10.ui.screens.AlimentacionScreen
import com.fic.dualhabit10.ui.screens.CalculadoraHidratacionScreen
import com.fic.dualhabit10.ui.screens.Forget_Password
import com.fic.dualhabit10.ui.screens.HabitosMascotaScreen
import com.fic.dualhabit10.ui.screens.HabitosScreen
import com.fic.dualhabit10.ui.screens.HidratacionMascotaScreen
import com.fic.dualhabit10.ui.screens.HidratacionScreen
import com.fic.dualhabit10.ui.screens.HistorialScreen
import com.fic.dualhabit10.ui.screens.InicioScreen
import com.fic.dualhabit10.ui.screens.LoginScreen
import com.fic.dualhabit10.ui.screens.MantenimientoSuenoScreen
import com.fic.dualhabit10.ui.screens.MascotasMenu
import com.fic.dualhabit10.ui.screens.PerfilMascotaScreen
import com.fic.dualhabit10.ui.screens.PerfilScreen
import com.fic.dualhabit10.ui.screens.RegisterScreen
import com.fic.dualhabit10.ui.screens.RegisterSuccessful
import com.fic.dualhabit10.ui.screens.ResultadoHidratacionScreen


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
                    composable("calculadora_agua"){
                        CalculadoraHidratacionScreen(navController = navController)
                    }
                    composable("resultado_hidratacion"){
                        ResultadoHidratacionScreen(navController = navController)
                    }
                    composable("historial") {
                        HistorialScreen(navController = navController)
                    }
                    composable("habitos_mascota_menu"){
                        HabitosMascotaScreen(navController)
                    }
                    composable("hidratacion_mascota"){
                        HidratacionMascotaScreen(navController = navController)
                    }
                    composable("perfil_mascota"){
                        PerfilMascotaScreen(navController = navController)
                    }
                    composable("actividad_fisica_mascota"){
                        ActividadFisicaScreen(navController = navController)
                    }
                    composable("alimentacion"){
                        AlimentacionScreen(navController = navController)
                    }
                    composable("alimentacion_mascota") {
                        AlimentacionMascotaScreen(navController = navController)
                    }
                    composable (
                        route = "receta_mascota_detalle/{recetaId}",
                        arguments = listOf(navArgument("recetaId") { type = NavType.StringType })
                    ){

                    }

                    composable("sueño"){
                        MantenimientoSuenoScreen(navController = navController)
                    }
                }
            }
        }
    }
}