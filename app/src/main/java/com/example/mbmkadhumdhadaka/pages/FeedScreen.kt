package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@Composable
fun FeedScreen(navController: NavController, authViewModel: AuthViewModel) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isClickToFeedback by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf("") }

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

        if (isClickToFeedback) {
            AlertDialog(
                onDismissRequest = { isClickToFeedback = false },
                title = { Text(text = "कैसा लगा ?") },
                text = {
                    Column {
                        Text(text = "कुछ मन में हो तो लिख दीजिए")
                        TextField(
                            value = rating,
                            onValueChange = { rating = it },
//                            label = { Text(text = "Rating") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Handle rating submission here
                            isClickToFeedback = false
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
