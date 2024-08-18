//import androidx.compose.ui.window.application
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.mbmkadhumdhadaka.R
import com.example.mbmkadhumdhadaka.Screens
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import com.example.mbmkadhumdhadaka.viewModel.UserDetailsViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.util.UUID

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

    // Fetch user details when the screen is loaded or when authentication state changes
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate(Screens.LgSpScreen.route) {
                popUpTo(Screens.ProfileScreen.route) { inclusive = true }
            }
        } else {
            fetchUserDetails(userDetailsViewModel, authViewModel) { details ->
                userProfile = details
            }
        }
    }

    // Update user profile data when userDetails changes
    LaunchedEffect(userDetailsViewModel.userDetails) {
        userProfile = userDetailsViewModel.userDetails.value
    }

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    // Function to handle image selection

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            EditProfileContent(
                userProfile = userProfile,
                selectedPhotoUri = selectedPhotoUri,
                onSave = { name, status ->
                    saveProfile(userDetailsViewModel, authViewModel, name, status, selectedPhotoUri, context)
                    scope.launch { sheetState.collapse() }
                },
                onCancel = { scope.launch { sheetState.collapse() } }
            )
        },
        sheetPeekHeight = 20.dp,
        topBar = {
            ProfileTopBar(
                onEditClick = {
                    if (sheetState.isCollapsed) {
                        scope.launch { sheetState.expand() }
                    } else {
                        scope.launch { sheetState.collapse() }
                    }
                },
                onRefreshClick = { fetchUserDetails(userDetailsViewModel, authViewModel) { details -> userProfile = details } }
            )
        }
    ) { innerPadding ->
        ProfileContent(
            userProfile = userProfile,
            selectedPhotoUri = selectedPhotoUri,
            photoPickerLauncher = photoPickerLauncher,
            onLogoutClick = { isShowLogoutDialog.value = true }
        )

        if (isShowLogoutDialog.value) {
            LogoutDialog(
                onConfirm = {
                    authViewModel.signOut()
                    isShowLogoutDialog.value = false
                },
                onDismiss = { isShowLogoutDialog.value = false }
            )
        }
    }
}

@Composable
fun EditProfileContent(
    userProfile: Map<String, Any>?,
    selectedPhotoUri: Uri?,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
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
            onValueChange = { if (it.length <= maxCharName) name = it },
            trailingIcon = { Text(text = "${name.length}/$maxCharName") },
            singleLine = true,
            label = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = status,
            onValueChange = { if (it.length <= maxCharStatus) status = it },
            trailingIcon = { Text(text = "${status.length}/$maxCharStatus") },
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
                modifier = Modifier.clickable { onCancel() }
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Save",
                modifier = Modifier.clickable { onSave(name, status) }
            )
        }
    }
}

@Composable
fun ProfileTopBar(onEditClick: () -> Unit, onRefreshClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Profile", modifier = Modifier.padding(start = 10.dp)) },
        actions = {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onRefreshClick) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }
    )
}

@Composable
fun ProfileContent(
    userProfile: Map<String, Any>?,
    selectedPhotoUri: Uri?,
    photoPickerLauncher: ActivityResultLauncher<String>,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var imageUrl: Uri? = null
        // Profile Picture
        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable {
                    photoPickerLauncher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
             imageUrl =
                selectedPhotoUri ?: (userProfile?.get("photoUrl") as? String)?.let { Uri.parse(it) }
            imageUrl.let {
            uri ->
            val documentId = UUID.randomUUID().toString() // Generate a random unique ID
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/$documentId.jpg")
            val uploadTask = uri?.let { imageRef.putFile(it) }

                uploadTask?.addOnSuccessListener {
                    // Image upload successful
                    Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener { e ->
                    // Image upload failed
                    Toast.makeText(context, "Image upload failed: $e", Toast.LENGTH_SHORT).show()
                }

        }
            if (imageUrl != null) {
                imageUrl?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .listener(
                                onStart = {
                                    Log.d(TAG, "Image loading started")
                                },
                                onSuccess = { request, metadata ->
                                    Log.d(TAG, "Image loading successful")
                                },
                                onError = { request, throwable ->
                                    Log.e(
                                        TAG,
                                        "Image loading failed: ${throwable.throwable.message ?: "Unknown error"}"
                                    )
                                }
                            )
                            .transformations(CircleCropTransformation())
                            .placeholder(R.drawable.ic_launcher_foreground) // Replace with your placeholder drawable
                            .error(R.drawable.ic_launcher_background) // Replace with your error drawable
                            .build(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                }
            } else {
                Log.d(TAG, "No image URL available")
                // Optionally display a default image or message
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with a default image
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
                Text(text = userProfile?.get("name")?.toString() ?: "No name set")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Status Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Status")
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Status:", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = userProfile?.get("status")?.toString() ?: "No status set")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Logout Button
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Logout") },
        text = { Text(text = "Are you sure you want to logout?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}


fun uploadImageToFirebase(
    imageUri: Uri,
    userId: String,
    onSuccess: (String) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    val imageRef: StorageReference = storageRef.child("profile_images/$userId.jpg")

    val uploadTask = imageRef.putFile(imageUri)

    uploadTask.addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            onSuccess(uri.toString())
        }
    }.addOnFailureListener { exception ->
        onFailure(exception)
    }
}

fun saveProfile(
    userDetailsViewModel: UserDetailsViewModel,
    authViewModel: AuthViewModel,
    name: String,
    status: String,
    photoUri: Uri?,
    context: Context
) {
    val email = authViewModel.auth.currentUser?.email
    val userId = authViewModel.auth.currentUser?.uid

    if (userId != null && email != null) {
        if (photoUri != null) {
            // Upload the image and save user details upon success
            uploadImageToFirebase(
                imageUri = photoUri,
                userId = userId,
                onSuccess = { photoUrl ->
                    // Save user details along with the uploaded image URL
                    userDetailsViewModel.saveUserDetails(
                        userId = userId,
                        name = name,
                        status = status,
                        photoUrl = photoUrl,
                        email = email
                    )
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            // Save user details without image
            userDetailsViewModel.saveUserDetails(
                userId = userId,
                name = name,
                status = status,
                photoUrl = null.toString(),
                email = email
            )
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "User not authenticated!", Toast.LENGTH_SHORT).show()
    }
}

fun fetchUserDetails(
    userDetailsViewModel: UserDetailsViewModel,
    authViewModel: AuthViewModel,
    onResult: (Map<String, Any>?) -> Unit
) {
    val user = authViewModel.auth.currentUser
    if (user != null) {
        userDetailsViewModel.getUserDetails(
            userId = user.uid)
    }
}


