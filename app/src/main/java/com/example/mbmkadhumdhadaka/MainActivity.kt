package com.example.mbmkadhumdhadaka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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


@Composable
fun LgSpScreen(navController: NavController){
    val registerClick = remember {
        mutableStateOf(true)
    }
    val loginClick = remember {
        mutableStateOf(false)
    }
   Column (modifier = Modifier.padding(50.dp)){
       Row (modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center){
           OutlinedButton(onClick = {
               registerClick.value = true
               loginClick.value = false
           }) {
               Text(text = "Register")
              
           }
         Spacer(modifier = Modifier.width(20.dp))
           OutlinedButton(onClick = {
               loginClick.value= true
               registerClick.value = false
           }) {
               Text(text = "Login")
               
           }
       }
       if(registerClick.value){
           ShowRegisterForm()
       }
       if(loginClick.value){
           ShowLoginForm()
       }
   }
}

@Composable
fun ShowRegisterForm(){

   Text(text = "hii this is your register page")
}
@Composable
fun ShowLoginForm(){
  Text(text = "hii this is your login page")
}

