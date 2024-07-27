package com.example.mbmkadhumdhadaka.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mbmkadhumdhadaka.dataModel.DummyData
import com.example.mbmkadhumdhadaka.dataModel.PostModel
import com.example.mbmkadhumdhadaka.viewModel.ReviewsViewModel

@Composable
fun FeedScreen(navController: NavController, reviewsViewModel: ReviewsViewModel) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isClickToFeedback by remember { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
//        background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                Text(
                    text = "MbmKaDhumDhadaka...",
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "refresh")
                }
            }

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)) {
                items(DummyData.posts) { item ->
                    PostCard(item)
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
                                reviewsViewModel.createFeedback(feedbackText)
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
fun PostCard(item: PostModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
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
                    text = "Owner Name",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.width(120.dp))
                IconButton(onClick = { /*TODO :open menu bar*/ }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "dots")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.postContent, style = TextStyle(fontSize = 14.sp))
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(item.postImage),
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Gray, RectangleShape)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Row{
           IconButton(onClick = { /*TODO*/ }) {
               Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "like")
           }
            Spacer(modifier = Modifier.width(10.dp))
//            Text(text = "Comment", modifier = Modifier.clickable { })
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "save post")
            }
            Spacer(modifier = Modifier.width(80.dp))
            Text("30 minutes ago")
        }
    }
}
