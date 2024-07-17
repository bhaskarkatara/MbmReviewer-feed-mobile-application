package com.example.mbmkadhumdhadaka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mbmkadhumdhadaka.ui.theme.MbmKaDhumDhadakaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MbmKaDhumDhadakaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LgSpScreen.route){
       composable(Screens.LgSpScreen.route){
           LgSpScreen(navController = navController)
       }
    }

}



