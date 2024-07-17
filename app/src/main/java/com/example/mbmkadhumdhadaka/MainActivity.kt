package com.example.mbmkadhumdhadaka

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mbmkadhumdhadaka.pages.FeedScreen
import com.example.mbmkadhumdhadaka.pages.LgSpScreen
import com.example.mbmkadhumdhadaka.ui.theme.MbmKaDhumDhadakaTheme
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            MbmKaDhumDhadakaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(authViewModel)
                }
            }
        }
    }

     fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            super.onNewIntent(intent)
        }
        // Handle the incoming intent if it's an email link
        handleEmailLink(intent)
    }

    private fun handleEmailLink(intent: Intent?) {
        val data = intent?.data
        val authViewModel = AuthViewModel()

        if (data != null && authViewModel.authState.isSignInWithEmailLink(data.toString())) {
            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("email", null)

            if (email != null) {
                authViewModel.signInWithEmailLink(email, data.toString())
            } else {
                // Prompt user to enter their email address if email not found in preferences
            }
        }
    }
}

@Composable
fun Navigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screens.LgSpScreen.route) {
        composable(Screens.LgSpScreen.route) {
            LgSpScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screens.FeedScreen.route) {
            FeedScreen(navController = navController, authViewModel = authViewModel)
            BackHandler {
                (context as MainActivity).finish()
            }
        }
    }
}
