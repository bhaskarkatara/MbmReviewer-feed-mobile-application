package com.example.mbmkadhumdhadaka

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mbmkadhumdhadaka.pages.CreatePost
import com.example.mbmkadhumdhadaka.pages.FeedScreen
import com.example.mbmkadhumdhadaka.pages.LgSpScreen
import com.example.mbmkadhumdhadaka.pages.ProfileScreen
import com.example.mbmkadhumdhadaka.pages.ReviewScreen
import com.example.mbmkadhumdhadaka.ui.theme.MbmKaDhumDhadakaTheme
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            MbmKaDhumDhadakaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(authViewModel = authViewModel)
                }
            }
        }
    }

    @Composable
    fun Navigation(authViewModel: AuthViewModel) {
        val navController = rememberNavController()
        val context = LocalContext.current
        var isCreatePostScreen by remember { mutableStateOf(false) }

        val authState = authViewModel.authState.observeAsState()

        Scaffold(
            bottomBar = {
                if (authState.value is AuthState.Authenticated && !isCreatePostScreen) {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screens.LgSpScreen.route,
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Padding to account for system UI
                    .navigationBarsPadding() // Add padding for system navigation bar
            ) {
                composable(Screens.LgSpScreen.route) {
                    LgSpScreen(navController = navController, authViewModel = authViewModel)
                    BackHandler {
                        (context as? Activity)?.finish()
                    }
                    isCreatePostScreen = false
                }
                composable(Screens.FeedScreen.route) {
                    FeedScreen(navController = navController, authViewModel = authViewModel)
                    BackHandler {
                        (context as? Activity)?.finish()
                    }
                    isCreatePostScreen = false
                }
                composable(Screens.ReviewScreen.route) {
                    ReviewScreen(navController = navController, authViewModel = authViewModel)
                    isCreatePostScreen = false
                }
                composable(Screens.ProfileScreen.route) {
                    ProfileScreen(navController = navController, authViewModel = authViewModel)
                    isCreatePostScreen = false
                }
                composable(Screens.CreatePostScreen.route) {
                    CreatePost(navController, authViewModel)
                    isCreatePostScreen = true
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavController) {
        BottomNavigation(
            modifier = Modifier.navigationBarsPadding(), // Ensure padding for the bottom navigation
            backgroundColor = Color(0xFFFFC0CB),
            contentColor = Color.Black
        ) {
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Feed") },
                label = { Text(text = "Feed") },
                selected = currentRoute == Screens.FeedScreen.route,
                onClick = {
                    navController.navigate(Screens.FeedScreen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Filled.ThumbUp, contentDescription = "Review") },
                label = { Text("Review") },
                selected = currentRoute == Screens.ReviewScreen.route,
                onClick = {
                    navController.navigate(Screens.ReviewScreen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = currentRoute == Screens.ProfileScreen.route,
                onClick = {
                    navController.navigate(Screens.ProfileScreen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
