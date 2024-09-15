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
    val context = LocalContext.current

    val postsData by postViewModel.postsData.observeAsState(PostResult.Loading)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                Text(
                    text = "MbmKaDhoomDhadaka...",
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(onClick = {
                    try {
                        postViewModel.loadPosts()
                    } catch (e: Exception) {
//                        Log.e(TAG, "FeedScreen: $e")
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "refresh")
                }
            }

            when (postsData) {
                is PostResult.Loading -> {
                    // Display loading indicator
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is PostResult.Success<*> -> {
                    val postList = (postsData as PostResult.Success<List<PostModel<Any?>>>).data ?: emptyList()
                    Log.d(TAG, "FeedScreenPosts: $postList")
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        if (postList.isEmpty()) {
                            item {
                                Text(
                                    text = "No post Available",
                                    modifier = Modifier.fillMaxWidth(),
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        } else {
                            items(postList, key = {it.postId}) { item ->
                                Log.d(TAG, "FeedScreen: $item")
                                PostCard(item, authViewModel)
                            }
                        }
                    }
//                    Log.d(TAG, "Loaded data: ${postList.size} posts")
                }
                is PostResult.Error -> {
                    // Display error message
                    Text(text = (postsData as PostResult.Error).exception, color = Color.Red)
                }
            }
        }

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

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            if (isMenuExpanded) {
                FloatingActionButton(
                    onClick = { isClickToFeedback = true },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Rate this app")
                }
                FloatingActionButton(
                    onClick = { navController.navigate("create_post_screen") },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Create Post")
                }
            }

            FloatingActionButton(
                onClick = { isMenuExpanded = !isMenuExpanded },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (isMenuExpanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Toggle Menu"
                )
            }
        }
    }
}

@Composable
fun PostCard(item: PostModel<Any?>,authViewModel: AuthViewModel) {
    val authState by authViewModel.authState.observeAsState()
    var expanded by remember { mutableStateOf(false) }
    val userId = authViewModel.auth.currentUser?.uid

    var likeCount by remember { mutableStateOf(0) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        val context = LocalContext.current
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(item.postOwnerPhoto),
                    contentDescription = "Owner Photo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .padding(end = 8.dp)
                )
                Text(
                    text = item.postOwnerName,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    if (userId != null) {
                        Toast.makeText(context, "work here :", Toast.LENGTH_SHORT).show()


                    }
                }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "dots")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.postContent, style = TextStyle(fontSize = 14.sp))
            Spacer(modifier = Modifier.height(8.dp))
//            var showFullScreenImage by remember { mutableStateOf(false) }

//            Log.d("PostCard", "PostCard: ${item.postImage}")
            Image(
                painter = rememberAsyncImagePainter(model = item.postImage,
                    onError = { Log.e(TAG, "Owner photo loading failed: ${it.result.throwable.message}")}
                        ),
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(0.dp, Color.Gray, RectangleShape).clickable {

                    }
            )

        }
  Log.d("PostCard", "PostCard: ${item.postImage}")
        Spacer(modifier = Modifier.height(3.dp))
        Row {
            IconButton(onClick = { likeCount += 1 } ) {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "like")
            }
            Spacer(modifier  = Modifier.width(10.dp))
//            Text(text = "${likeCount}")
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "save post")
            }
            Spacer(modifier = Modifier.width(80.dp))
            Text(text = formatTimeStamp(item.timestamp))
        }
    }
}
fun formatTimeStamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

@Composable
fun ZoomableImage(imageUrl: String) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
            .graphicsLayer(
                scaleX = maxOf(1f, minOf(3f, scale)),
                scaleY = maxOf(1f, minOf(3f, scale)),
                translationX = offsetX,
                translationY = offsetY
            )
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Zoomable Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}