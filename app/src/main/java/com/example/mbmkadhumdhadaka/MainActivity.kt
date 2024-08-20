package com.example.mbmkadhumdhadaka

//import com.example.mbmkadhumdhadaka.pages.ProfileScreen
import ProfileScreen
import android.app.Activity
import android.content.ContentValues.TAG
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
import com.example.mbmkadhumdhadaka.pages.ReviewScreen
import com.example.mbmkadhumdhadaka.pages.SplashScreen
import com.example.mbmkadhumdhadaka.ui.theme.MbmKaDhumDhadakaTheme
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import com.example.mbmkadhumdhadaka.viewModel.PostViewModel
import com.example.mbmkadhumdhadaka.viewModel.ReviewsViewModel
import com.example.mbmkadhumdhadaka.viewModel.UserDetailsViewModel

// todo : solve error in profile screen after logout the first user details should be erase/clear
// todo: add circular progress bar when post is clicked
// todo : remove picture from create post screen ( picture which is posted by another user)
// todo: logout means erase all the data of current user / completely new interface for new user
//todo: fix this: old post images is replacing with new post images
// todo : fix userid == post id in feed screen , in firebase all stuff is right but i received post id and user id different -2
//todo : implement like feature

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val reviewViewModel: ReviewsViewModel = viewModel()

            MbmKaDhumDhadakaTheme {
                MainContent(authViewModel, reviewViewModel)
            }
        }
    }

    @Composable
    fun MainContent(authViewModel: AuthViewModel, reviewsViewModel: ReviewsViewModel) {
        var selectedPhotoUriForProfile by remember { mutableStateOf<Uri?>(null) }
        var selectedPhotoUriForPost by remember { mutableStateOf<Uri?>(null) }
        var selectedVideoUriForPost by remember { mutableStateOf<Uri?>(null) }

        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { selectedPhotoUriForProfile = it }
        }

        val photoPickerLauncherForPost = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { selectedPhotoUriForPost = it }
        }

        val videoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { selectedVideoUriForPost = it }
        }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Navigation(
                authViewModel = authViewModel,
                reviewsViewModel = reviewsViewModel,
                photoPickerLauncher = photoPickerLauncher,
                videoPickerLauncher = videoPickerLauncher,
                selectedPhotoUriForProfile = selectedPhotoUriForProfile,
                setSelectedPhotoUriForProfile = { selectedPhotoUriForProfile = it },
                selectedVideoUri = selectedVideoUriForPost,
                setSelectedVideoUri = { selectedVideoUriForPost = it },
                photoPickerLauncherForPost = photoPickerLauncherForPost,
                selectedPhotoUriForPost = selectedPhotoUriForPost,
                setSelectedPhotoUriForPost = { selectedPhotoUriForPost = it }
            )
        }
    }

    @Composable
    fun Navigation(
        authViewModel: AuthViewModel,
        reviewsViewModel: ReviewsViewModel,
        photoPickerLauncher: ActivityResultLauncher<String>,
        videoPickerLauncher: ActivityResultLauncher<String>,
        selectedPhotoUriForProfile: Uri?,
        setSelectedPhotoUriForProfile: (Uri?) -> Unit,
        selectedVideoUri: Uri?,
        setSelectedVideoUri: (Uri?) -> Unit,
        photoPickerLauncherForPost: ActivityResultLauncher<String>,
        selectedPhotoUriForPost: Uri?,
        setSelectedPhotoUriForPost: (Uri?) -> Unit
    ) {
        val navController = rememberNavController()
        val context = LocalContext.current
        var isCreatePostScreen by remember { mutableStateOf(false) }
        var isCreateReviewScreen by remember { mutableStateOf(false) }
        var isSplashScreen by remember { mutableStateOf(true) }

        val authState = authViewModel.authState.observeAsState()
        val userDetailViewModel: UserDetailsViewModel = viewModel()
        val postViewModel: PostViewModel = viewModel()

        Scaffold(
            bottomBar = {
                if (authState.value is AuthState.Authenticated && !isCreatePostScreen &&
                    (authState.value is AuthState.Authenticated && !isCreateReviewScreen) && !isSplashScreen) {
                    BottomNavigationBar(navController, userDetailViewModel, authViewModel)
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
                    FeedScreen(navController = navController, postViewModel = postViewModel)
                    BackHandler {
                        (context as? Activity)?.finish()
                    }
                    isCreatePostScreen = false
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.ReviewScreen.route) {
                    ReviewScreen(navController = navController, reviewsViewModel = reviewsViewModel)
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
//                        setSelectedPhotoUri = setSelectedPhotoUriForProfile
                    )
                    isCreatePostScreen = false
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.CreatePostScreen.route) {
                    CreatePost(
                        navController = navController,
                        photoPickerLauncher = photoPickerLauncher,
                        videoPickerLauncher = videoPickerLauncher,
                        selectedPhotoUri = selectedPhotoUriForProfile,
                        setSelectedPhotoUri = setSelectedPhotoUriForProfile,
                        selectedVideoUri = selectedVideoUri,
                        setSelectedVideoUri = setSelectedVideoUri,
                        photoPickerLauncherForPost = photoPickerLauncherForPost,
                        selectedPhotoUriForPost = selectedPhotoUriForPost,
                        setSelectedPhotoUriForPost = setSelectedPhotoUriForPost,
                        userDetailViewModel,
                        postViewModel = postViewModel,
                        authViewModel
                    )
                    isCreatePostScreen = true
                    isCreateReviewScreen = false
                    isSplashScreen = false
                }
                composable(Screens.CreateReviewScreen.route) {
                    CreateReviewScreen(navController, reviewsViewModel)
                    isCreatePostScreen = false
                    isCreateReviewScreen = true
                    isSplashScreen = false
                }
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

