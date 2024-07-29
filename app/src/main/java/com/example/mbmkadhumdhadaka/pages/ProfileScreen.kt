package com.example.mbmkadhumdhadaka.pages

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
    photoPickerLauncher: ActivityResultLauncher<Intent>,
    selectedPhotoUri: Uri?,
    setSelectedPhotoUri: (Uri?) -> Unit
) {
    Log.d(TAG, "ProfileScreen: Main screen here")
    val isShowLogoutDialog = remember { mutableStateOf(false) }
    val authState by authViewModel.authState.observeAsState()
    var loader by remember { mutableStateOf(true) }

    // State for user profile
    var userProfile by remember { mutableStateOf<Map<String, Any>?>(null) }

    // Fetch user details when the screen is loaded or when authentication state changes
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate(Screens.LgSpScreen.route) {
                popUpTo(Screens.ProfileScreen.route) { inclusive = true }
            }
        } else {
            loader = true
            val userId = authViewModel.auth.currentUser?.uid
            if (userId != null) {
                userDetailsViewModel.getUserDetails(userId)
            }
        }
    }

    // Update user profile data when userDetails changes
    LaunchedEffect(userDetailsViewModel.userDetails) {
        userProfile = userDetailsViewModel.userDetails.value
        loader = false
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
                                loader = true // Start loader when saving
                                val userId = authViewModel.auth.currentUser?.uid
                                if (userId != null) {
                                    userDetailsViewModel.saveUserDetails(
                                        userId,
                                        name ?: "Your_name",
                                        status ?: "Your_status",
                                        selectedPhotoUri?.toString() ?: "",
                                        authViewModel.auth.currentUser!!.email.toString()
                                    )
                                    loader = false // Stop loader after saving
                                    sheetState.collapse()
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
                }
            )
        }
    ) { innerPadding ->
        if (loader) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        } else {
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
                            val intent = Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            photoPickerLauncher.launch(intent)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data(selectedPhotoUri ?: userProfile?.get("photoUrl"))
                        .placeholder(R.drawable.ic_launcher_foreground) // Optional placeholder image
                        .error(R.drawable.ic_launcher_foreground).build() // Optional error image
                    Image(
                        painter = rememberAsyncImagePainter(imageRequest),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = userProfile?.get("name")?.toString() ?: "Loading...")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Status")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Status:", style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = userProfile?.get("status")?.toString() ?: "Loading...")
                    }
                }

                Spacer(modifier = Modifier.height(130.dp))

                Button(onClick = {
                    isShowLogoutDialog.value = true
                }) {
                    Text(text = "Log Out")
                }

                Spacer(modifier = Modifier.height(100.dp))

                Text(
                    text = "--Mugneeram Ji--",
                    style = MaterialTheme.typography.h6,
                    color = Color.Magenta
                )
            }
        }
    }

    if (isShowLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { isShowLogoutDialog.value = false },
            title = { Text(text = "Log Out") },
            text = { Text(text = "Are you sure you want to log out?") },
            confirmButton = {
                Button(
                    onClick = {
                        isShowLogoutDialog.value = false
                        authViewModel.signOut()
                    }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        isShowLogoutDialog.value = false
                    }) {
                    Text(text = "No")
                }
            }
        )
    }
}
