package com.example.mbmkadhumdhadaka.pages

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.mbmkadhumdhadaka.R
import com.example.mbmkadhumdhadaka.dataModel.PostModel
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import com.example.mbmkadhumdhadaka.viewModel.PostResult
import com.example.mbmkadhumdhadaka.viewModel.PostViewModel
import com.example.mbmkadhumdhadaka.viewModel.SharedViewModel
import com.example.mbmkadhumdhadaka.viewModel.UserDetailsViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

@Composable
fun CreatePost(
    navController: NavController,
    photoPickerLauncher: ActivityResultLauncher<String>,
    videoPickerLauncher: ActivityResultLauncher<String>,
    selectedPhotoUri: Uri?,
    setSelectedPhotoUri: (Uri?) -> Unit,
    selectedVideoUri: Uri?,
    setSelectedVideoUri: (Uri?) -> Unit,
    photoPickerLauncherForPost: ActivityResultLauncher<String>,
    selectedPhotoUriForPost: Uri?,
    setSelectedPhotoUriForPost: (Uri?) -> Unit,
    userDetailsViewModel: UserDetailsViewModel,
    postViewModel: PostViewModel,
    authViewModel: AuthViewModel,
    sharedViewModel: SharedViewModel
) {
    var postContent by rememberSaveable { mutableStateOf("") }
    var isShowMedia by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val userId = authViewModel.auth.currentUser?.uid

    val imageUrl by sharedViewModel.imageUrl.observeAsState()
    val newPostId = UUID.randomUUID().toString()

    var isLoading by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = {
                            if (postContent.isNotEmpty() && userDetailsViewModel.userDetails.value != null ) {
                                selectedPhotoUriForPost?.let { uri ->
                                    isLoading = true;
                                    uploadPostToFirebase(
                                        context = context,
                                        imageUri = uri,
//                                        userId = userId!!,
                                        postId = newPostId,
                                        onSuccess = { imageUrl ->
                                            postViewModel.createPost(
                                                PostModel(
                                                    postId = newPostId, // todo: generate new post Id
                                                    postContent = postContent,
                                                    postImage = imageUrl,
                                                    postOwnerPhoto = userDetailsViewModel.userDetails.value?.get("photoUrl").toString(),
                                                    postOwnerName = userDetailsViewModel.userDetails.value?.get("name").toString(),
                                                    userId = userId,
                                                    timestamp = System.currentTimeMillis()
                                                ),

                                            )
                                                isLoading = false;
//                                            sharedViewModel.setImageUrl(null)
                                            postContent = ""
                                            setSelectedPhotoUriForPost(null)
                                            navController.navigateUp()
                                        },
                                        onFailure = {
                                            // Handle failure case if needed
                                        }
                                    )
                                } ?: run {
                                    Toast.makeText(
                                        context,
                                        "Please select an image before posting.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }else if(userDetailsViewModel.userDetails.value == null){
                                Toast.makeText(context,"Please load name in profile",Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (postContent.isNotEmpty()) Color.Red else Color.Transparent
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            Toast.makeText(context,"please don't press back button",Toast.LENGTH_SHORT).show()
                        }

                        Text(text = "Post")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            imageUrl?.let { url ->
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }
                ?: run {
                    // Handle the case where imageUrl is null
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground), // Use a placeholder image
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                }
            Spacer(modifier = Modifier.width(10.dp))
         val name = userDetailsViewModel.userDetails.value?.get("name").toString()


        Text(
            text = if (name.isBlank() || name == "null") "Anonymous" else name,
            style = MaterialTheme.typography.h6
        )

            Spacer(modifier = Modifier.height(20.dp))

            // Text Field for Post Content
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.1f), RectangleShape)
                    .border(1.dp, Color.Gray, RectangleShape)
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = postContent,
                    onValueChange = { postContent = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxSize()
                )
                if (postContent.isEmpty()) {
                    Text(
                        text = "What's on your mind",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Button to Show Media Options
            IconButton(onClick = { isShowMedia = !isShowMedia }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Pick Media")
            }

            if (isShowMedia) {
                Column {
                    Text(
                        text = "Photo",
                        modifier = Modifier
                            .clickable {
                                photoPickerLauncherForPost.launch("image/*")
                                isShowMedia = false
                            }
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // Uncomment if video functionality is needed
//                    Text(
//                        text = "Video",
//                        modifier = Modifier
//                            .clickable {
//                                videoPickerLauncher.launch("video/*")
//                            }
//                            .padding(8.dp)
//                    )
                }
            }

            // Display Selected Post Photo
            selectedPhotoUriForPost?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Post Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp)
                        .border(1.dp, Color.Gray)
                )
            }
            if (selectedPhotoUriForPost != null) {
                Text(
                    text = "Delete Image",
                    modifier = Modifier
                        .clickable { setSelectedPhotoUriForPost(null) }
                        .padding(top = 8.dp)
                )
            }

            // Display Selected Video
            selectedVideoUri?.let { uri ->
                Text(
                    text = "Selected Video: $uri",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(Color.Gray.copy(alpha = 0.1f))
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                )
            }
        }
    }
}

fun uploadPostToFirebase(
    context: Context,
    imageUri: Uri,
//    userId: String,
    postId: String,
    onSuccess: (String) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    val imageRef: StorageReference = storageRef.child("post_images/$postId.jpg")

    val uploadTask = imageRef.putFile(imageUri)

    uploadTask.addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            onSuccess(uri.toString())
        }.addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to retrieve image URL: ${exception.message}", Toast.LENGTH_SHORT).show()
            onFailure(exception)
        }
    }.addOnFailureListener { exception ->
        Toast.makeText(context, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
        onFailure(exception)
    }
}
