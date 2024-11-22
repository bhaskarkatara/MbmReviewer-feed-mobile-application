package com.example.mbmkadhumdhadaka.pages

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mbmkadhumdhadaka.dataModel.PostModel
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import com.example.mbmkadhumdhadaka.viewModel.PostResult
import com.example.mbmkadhumdhadaka.viewModel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FeedScreen(navController: NavController, postViewModel: PostViewModel,authViewModel: AuthViewModel) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isClickToFeedback by remember { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf("") }
    var fullScreenImageUrl by remember { mutableStateOf<String?>(null) } // State to handle full-screen image
    val context = LocalContext.current
    val postsData by postViewModel.postsData.observeAsState(PostResult.Loading)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MbmKaDhoomDhadaka...",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    try {
                        postViewModel.loadPosts()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }

            // Posts Content
            when (postsData) {
                is PostResult.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is PostResult.Success<*> -> {
                    val postList = (postsData as PostResult.Success<List<PostModel<Any?>>>).data ?: emptyList()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    ) {
                        if (postList.isEmpty()) {
                            item {
                                Text(
                                    text = "No posts available",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        } else {
                            items(postList, key = { it.postId }) { item ->
                                PostCard(item, authViewModel) { imageUrl ->
                                    fullScreenImageUrl = imageUrl
                                }
                            }
                        }
                    }
                }
                is PostResult.Error -> Text(
                    text = (postsData as PostResult.Error).exception,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Full-Screen Image Dialog
        fullScreenImageUrl?.let { imageUrl ->
            Dialog(onDismissRequest = { fullScreenImageUrl = null }) {
                ZoomableImage(imageUrl = imageUrl, onClose = { fullScreenImageUrl = null })
            }
        }

        // Floating Action Buttons
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            if (isClickToFeedback) {
                AlertDialog(
                    onDismissRequest = { isClickToFeedback = false },
                    title = { Text(text = "कैसा लगा ?") },
                    text = {
                        Column {
                            Text(text = "कुछ मन में हो तो लिख दीजिए")
                            TextField(
                                value = feedbackText,
                                onValueChange = { feedbackText = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (feedbackText.isNotEmpty()) {
                                    // Replace with actual feedback function
                                    Toast.makeText(context, "Thank You :)", Toast.LENGTH_SHORT).show()
                                    feedbackText = "" // Clear the feedback text after submission
                                    isClickToFeedback = false
                                }
                            }
                        ) {
                            Text(text = "Rate")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isClickToFeedback = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
            if (isMenuExpanded) {
                FloatingActionButton(onClick = { isClickToFeedback = true }) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Rate this app")
                }
                FloatingActionButton(onClick = { navController.navigate("create_post_screen") }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Create Post")
                }
            }
            FloatingActionButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(imageVector = if (isMenuExpanded) Icons.Default.Close else Icons.Default.Add, contentDescription = "Toggle Menu")
            }
        }
    }
}
@Composable
fun PostCard(item: PostModel<Any?>, authViewModel: AuthViewModel,onImageClick: (String) -> Unit) {
    val userId = authViewModel.auth.currentUser?.uid
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf( 0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.postOwnerPhoto),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = item.postOwnerName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = formatTimeStamp(item.timestamp),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* TODO: Show post options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options"
                    )
                }
            }

            Text(
                text = item.postContent,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Image(
                painter = rememberAsyncImagePainter(model = item.postImage),
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RectangleShape)
                    .clickable { onImageClick(item.postImage ?: "") },
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        isLiked = !isLiked
                        likeCount += if (isLiked) 1 else -1
                    }) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.FavoriteBorder else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.Gray
                        )
                    }
                    Text(text = "$likeCount likes", style = MaterialTheme.typography.bodySmall)
                }
                TextButton(onClick = { /* TODO: Show comments */ }) {
                    Text(text = "Comments", style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { /* TODO: Save post */ }) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Save"
                    )
                }
            }
        }
    }
}

fun formatTimeStamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
@Composable
fun ZoomableImage(imageUrl: String, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.Black)
            .clickable { onClose() }
    ) {
        var scale by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Zoomable Image",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale.coerceIn(1f, 3f),
                    scaleY = scale.coerceIn(1f, 3f),
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                },
            contentScale = ContentScale.Fit
        )
    }
}