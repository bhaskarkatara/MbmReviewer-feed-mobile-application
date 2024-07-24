package com.example.mbmkadhumdhadaka.pages

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mbmkadhumdhadaka.viewModel.ReviewsViewModel
import com.google.firebase.database.FirebaseDatabase

@Composable
fun FeedScreen(navController: NavController,reviewsViewModel: ReviewsViewModel) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isClickToFeedback by remember { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf("") }
    val context = LocalContext.current
    

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
        Box {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
                Text(text = "MbmKaDhumDhadaka...", style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,

                ))
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
                            // Submit feedback to Firebase
                            if (feedbackText.isNotEmpty()) {
                              reviewsViewModel.createFeedback(feedbackText)
                                Toast.makeText(context, "Thank You :)", Toast.LENGTH_SHORT).show()
//                                Log.d(TAG, "FeedbackScreen: $userRef")
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
    }
}
