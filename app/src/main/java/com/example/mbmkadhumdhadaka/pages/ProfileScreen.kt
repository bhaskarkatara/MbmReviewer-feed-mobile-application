import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.mbmkadhumdhadaka.R
import com.example.mbmkadhumdhadaka.Screens
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import com.example.mbmkadhumdhadaka.viewModel.UserDetailsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    userDetailsViewModel: UserDetailsViewModel,
    navController: NavController,
    authViewModel: AuthViewModel,
    photoPickerLauncher: ActivityResultLauncher<String>,
    selectedPhotoUri: Uri?,
    setSelectedPhotoUri: (Uri?) -> Unit
) {
    val isShowLogoutDialog = remember { mutableStateOf(false) }
    val authState by authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // State for user profile
    var userProfile by remember { mutableStateOf<Map<String, Any>?>(null) }

////    Log.d(TAG, "ProfileScreen: first step")
//    // Permission handling
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (!isGranted) {
//            Log.d(TAG, "Permission denied.")
//            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            Log.d(TAG, "Permission granted.")
//        }
//    }
//    LaunchedEffect(Unit) {
//        try {
//            Log.d(TAG, "Checking permission...")
//            if (ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                Log.d(TAG, "Requesting permission...")
//                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//            }
//        } catch (e: Exception) {
//            Log.e(TAG, "Permission request error: ${e.message}")
//        }
//    }



    fun fetchUserDetails() {
        val userId = authViewModel.auth.currentUser?.uid
        if (userId != null) {
            userDetailsViewModel.getUserDetails(userId)
        }
    }


    // Fetch user details when the screen is loaded or when authentication state changes
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate(Screens.LgSpScreen.route) {
                popUpTo(Screens.ProfileScreen.route) { inclusive = true }
            }
        } else {
            fetchUserDetails()
        }
    }

    // Update user profile data when userDetails changes
    LaunchedEffect(userDetailsViewModel.userDetails) {
        userDetailsViewModel.userDetails.value?.let { details ->
            userProfile = details
        }
    }

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Edit Profile", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))

                var name by remember { mutableStateOf(userProfile?.get("name")?.toString() ?: "") }
                var status by remember { mutableStateOf(userProfile?.get("status")?.toString() ?: "") }
                val maxCharName = 15
                val maxCharStatus = 20

                TextField(
                    value = name,
                    onValueChange = {
                        if (it.length <= maxCharName) {
                            name = it
                        }
                    },
                    trailingIcon = {
                        Text(text = "${name.length}/${maxCharName}")
                    },
                    singleLine = true,
                    label = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = status,
                    onValueChange = {
                        if (it.length <= maxCharStatus) {
                            status = it
                        }
                    },
                    trailingIcon = {
                        Text(text = "${status.length}/${maxCharStatus}")
                    },
                    singleLine = true,
                    label = { Text(text = "Status") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancel",
                        modifier = Modifier.clickable { scope.launch { sheetState.collapse() } }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "Save",
                        modifier = Modifier.clickable {
                            scope.launch {
                                val userId = authViewModel.auth.currentUser?.uid
                                if (userId != null && name.isNotEmpty() && status.isNotEmpty()) {
                                    userDetailsViewModel.saveUserDetails(
                                        userId,
                                        name,
                                        status,
                                        selectedPhotoUri?.toString() ?: "",
                                        authViewModel.auth.currentUser!!.email.toString()
                                    )
                                    fetchUserDetails() // Trigger a reload after saving
                                    sheetState.collapse()
                                } else {
                                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        },
        sheetPeekHeight = 20.dp,
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile", modifier = Modifier.padding(start = 10.dp)) },
                actions = {
                    IconButton(onClick = {
                        if (sheetState.isCollapsed) {
                            scope.launch { sheetState.expand() }
                        } else {
                            scope.launch { sheetState.collapse() }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { fetchUserDetails() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "image/*"
                        }
                        photoPickerLauncher.launch("image/*")


                    },
                contentAlignment = Alignment.Center
            ) {
                val imageUrl = selectedPhotoUri ?: userProfile?.get("photoUrl")
                Log.d(TAG, "Image URL: $imageUrl")

                if (imageUrl != null) {
                    val imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .listener(
                            onStart = {
                                Log.d(TAG, "Image loading started")
                            },
                            onSuccess = { request, metadata ->
                                Log.d(TAG, "Image loading successful")
                            },
                            onError = { request, throwable ->
                                Log.e(TAG, "Image loading failed: ${throwable.throwable.message ?: "Unknown error"}")
                            }
                        )
                        .placeholder(R.drawable.ic_launcher_foreground) // Optional placeholder image
                        .error(R.drawable.ic_launcher_foreground) // Optional error image
                        .transformations(CircleCropTransformation())
                        .build()

                    AsyncImage(
                        model = imageRequest,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Name Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Name")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Name:", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = userProfile?.get("name")?.toString() ?: "Loading...", style = MaterialTheme.typography.h6)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Status Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "Status")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Status:", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = userProfile?.get("status")?.toString() ?: "Loading...", style = MaterialTheme.typography.h6)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { isShowLogoutDialog.value = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Logout")
            }

            if (isShowLogoutDialog.value) {
                AlertDialog(
                    onDismissRequest = { isShowLogoutDialog.value = false },
                    title = { Text(text = "Logout") },
                    text = { Text(text = "Are you sure you want to logout?") },
                    confirmButton = {
                        TextButton(onClick = {
                            authViewModel.signOut()
                            isShowLogoutDialog.value = false
                        }) {
                            Text(text = "Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isShowLogoutDialog.value = false }) {
                            Text(text = "No")
                        }
                    }
                )
            }
        }
    }

    // Request permission if not already granted
//    if (ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//
//            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//
//
//    }
}
