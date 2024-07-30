package com.example.mbmkadhumdhadaka

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mbmkadhumdhadaka.pages.CreatePost
import com.example.mbmkadhumdhadaka.pages.CreateReviewScreen
import com.example.mbmkadhumdhadaka.pages.FeedScreen
import com.example.mbmkadhumdhadaka.pages.LgSpScreen
import com.example.mbmkadhumdhadaka.pages.ProfileScreen
import com.example.mbmkadhumdhadaka.pages.ReviewScreen
import com.example.mbmkadhumdhadaka.pages.SplashScreen
import com.example.mbmkadhumdhadaka.ui.theme.MbmKaDhumDhadakaTheme
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import com.example.mbmkadhumdhadaka.viewModel.PostViewModel
import com.example.mbmkadhumdhadaka.viewModel.ReviewsViewModel
import com.example.mbmkadhumdhadaka.viewModel.UserDetailsViewModel

class MainActivity : ComponentActivity() {
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var videoPickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var photoPickerLauncherForPost: ActivityResultLauncher<Intent>

    private var selectedPhotoUriForProfile by mutableStateOf<Uri?>(null)
    private var selectedVideoUriForPost by mutableStateOf<Uri?>(null)
    private var selectedPhotoUriForPost by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedPhotoUriForProfile = selectedImageUri
            }
        }
        photoPickerLauncherForPost = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedPhotoUriForPost = selectedImageUri
            }
        }


        videoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedVideoUri = result.data?.data
                this@MainActivity.selectedVideoUriForPost = selectedVideoUri
            }
        }

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val reviewViewModel : ReviewsViewModel = viewModel()

            MbmKaDhumDhadakaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                         authViewModel=  authViewModel,
                         reviewViewModel ,
                        photoPickerLauncher = photoPickerLauncher,
                        videoPickerLauncher = videoPickerLauncher,
                        selectedPhotoUriForProfile = selectedPhotoUriForProfile,
                        setselectedPhotoUriForProfile = { selectedPhotoUriForProfile = it },
                        selectedVideoUri = selectedVideoUriForPost,
                        setSelectedVideoUri = { selectedVideoUriForPost = it },
                        photoPickerLauncherForPost = photoPickerLauncherForPost,
                        selectedPhotoUriForPost = selectedPhotoUriForPost,
                        setSelectedPhotoUriForPost = { selectedPhotoUriForPost = it }

                    )
                }
            }
        }
    }


    @Composable
    fun Navigation(authViewModel: AuthViewModel,reviewsViewModel: ReviewsViewModel,
                   photoPickerLauncher: ActivityResultLauncher<Intent>,
                   videoPickerLauncher: ActivityResultLauncher<Intent>,
                   selectedPhotoUriForProfile: Uri?,
                   setselectedPhotoUriForProfile: (Uri?) -> Unit,
                   selectedVideoUri: Uri?,
                   setSelectedVideoUri: (Uri?) -> Unit,
                   photoPickerLauncherForPost: ActivityResultLauncher<Intent>,
                   selectedPhotoUriForPost: Uri?,
                   setSelectedPhotoUriForPost: (Uri?) -> Unit
    )
    {
        val navController = rememberNavController()
        val context = LocalContext.current
        var isCreatePostScreen by remember { mutableStateOf(false) }
        var isCreateReviewScreen by remember { mutableStateOf(false) }
        var isSplashScreen by remember { mutableStateOf(true) }

        val authState = authViewModel.authState.observeAsState()
        val userDetailViewModel : UserDetailsViewModel = viewModel()
         val postViewModel : PostViewModel = viewModel()


        Scaffold(
            bottomBar = {
                if (authState.value is AuthState.Authenticated && !isCreatePostScreen &&
                    (authState.value is AuthState.Authenticated && !isCreateReviewScreen) && !isSplashScreen ) {
                    BottomNavigationBar(navController,userDetailViewModel,authViewModel)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screens.SplashScreen.route,
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Padding to account for system UI
                    .navigationBarsPadding() // Add padding for system navigation bar
            ) {
                composable("splash_screen") {
                    SplashScreen(navController, authViewModel)
                    isCreatePostScreen = false
                    isCreateReviewScreen = false
                    isSplashScreen = true
                }
                composable(Screens.LgSpScreen.route) {
                    LgSpScreen(navController = navController, authViewModel = authViewModel)
                    BackHandler {
                        (context as? Activity)?.finish()
                    }
                    isCreatePostScreen = false
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.FeedScreen.route) {
                    FeedScreen(navController = navController,postViewModel = postViewModel)
                    BackHandler {
                        (context as? Activity)?.finish()
                    }
                    isCreatePostScreen = false
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.ReviewScreen.route) {
                    ReviewScreen(navController = navController,reviewsViewModel = reviewsViewModel)
                    isCreatePostScreen = false
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.ProfileScreen.route) {
                    ProfileScreen(
                        userDetailViewModel,
                        navController = navController,
                        authViewModel = authViewModel,
                        photoPickerLauncher = photoPickerLauncher,
                        selectedPhotoUri = selectedPhotoUriForProfile,
                        setSelectedPhotoUri = setselectedPhotoUriForProfile
                        )

                    isCreatePostScreen = false
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.CreatePostScreen.route) {
                    CreatePost( navController = navController,
                        photoPickerLauncher = photoPickerLauncher,
                        videoPickerLauncher = videoPickerLauncher,
                        selectedPhotoUri = selectedPhotoUriForProfile,
                        setSelectedPhotoUri = setselectedPhotoUriForProfile,
                        selectedVideoUri = selectedVideoUri,
                        setSelectedVideoUri = setSelectedVideoUri,
                        photoPickerLauncherForPost = photoPickerLauncherForPost,
                        selectedPhotoUriForPost = selectedPhotoUriForPost,
                        setSelectedPhotoUriForPost = setSelectedPhotoUriForPost,
                        userDetailViewModel,
                        postViewModel = postViewModel
                        )
                    isCreatePostScreen = true
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.CreateReviewScreen.route){
                    CreateReviewScreen(navController, reviewsViewModel)
                    isCreatePostScreen = false
                    isCreateReviewScreen = true
                    isSplashScreen = false
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavController,userDetailsViewModel: UserDetailsViewModel,authViewModel: AuthViewModel) {
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
                    try{
                        userDetailsViewModel.getUserDetails(authViewModel.auth.currentUser!!.uid)
                        navController.navigate(Screens.ProfileScreen.route) {

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    catch (e: Exception){
                        Log.d(TAG, "BottomNavigationBar: ${e.message}")
                    }

                }
            )
        }
    }
}
