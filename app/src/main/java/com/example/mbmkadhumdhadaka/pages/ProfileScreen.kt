package com.example.mbmkadhumdhadaka.pages

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import com.example.mbmkadhumdhadaka.R
import com.example.mbmkadhumdhadaka.Screens
import com.example.mbmkadhumdhadaka.viewModel.AuthState
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    photoPickerLauncher: ActivityResultLauncher<Intent>,
    selectedPhotoUri: Uri?,
    setSelectedPhotoUri: (Uri?) -> Unit
) {
    val isShowLogoutDialog = remember { mutableStateOf(false) }
    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate(Screens.LgSpScreen.route) {
                popUpTo(Screens.ProfileScreen.route) { inclusive = true }
            }
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

                var name by remember { mutableStateOf("") }
                var status by remember { mutableStateOf("") }
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
                            // TODO: Save profile changes
                            scope.launch { sheetState.collapse() }
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
                        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        photoPickerLauncher.launch(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = selectedPhotoUri?.let { rememberAsyncImagePainter(it) }
                        ?: painterResource(id = R.drawable.ic_launcher_foreground), // Default image
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
                    .padding(start = 20.dp), horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Name")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Name:", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "your_name") // Replace with user name
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "Status")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Status:", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "your_status") // Replace with user status
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

    if (isShowLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { isShowLogoutDialog.value = false },
            title = { Text(text = "Logout?") },
            text = { Text(text = "Are you sure you want to logout?") },
            confirmButton = {
                Button(onClick = {
                    isShowLogoutDialog.value = false
                    authViewModel.signOut()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = {
                    isShowLogoutDialog.value = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
