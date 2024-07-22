package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mbmkadhumdhadaka.dataModel.DummyData
import com.example.mbmkadhumdhadaka.dataModel.ReviewModel
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(navController: NavController, authViewModel: AuthViewModel) {
    var filteredReviews by remember { mutableStateOf(DummyData.reviews) }
    var showFilterDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Reviews") },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 5.dp)
            ) {
                items(filteredReviews) { review ->
                    ReviewCard(review = review)
                }
            }

            FloatingActionButton(
                onClick = {  navController.navigate("create_review_screen")},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Sharp.Add,
                    contentDescription = "Add Review"
                )
            }
        }
    }


    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApplyFilter = { tag ->
                filteredReviews = if (tag.isEmpty()) {
                    DummyData.reviews
                } else {
                    DummyData.reviews.filter { it.tagPlaces == tag }
                }
                showFilterDialog = false
            }
        )
    }
}

@Composable
fun ReviewCard(review: ReviewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = review.reviewText,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Rating: ${review.ratingStar} stars",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Tag: ${review.tagPlaces}",
                color = Color.Blue,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
//@Composable
fun FilterDialog(onDismiss: () -> Unit, onApplyFilter: (String) -> Unit) {
    var selectedTag by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Tag") },
        text = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)) {

                listOf("Mess", "Hostel", "Canteen", "Library").forEach { tag ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedTag == tag,
                            onClick = { selectedTag = tag }
                        )
                        Text(
                            text = tag,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
//                if(selectedTag.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        RadioButton(
                            selected = selectedTag.isEmpty(),
                            onClick = { selectedTag = "" }
                        )
                        Text(
                            text = "All tags",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
//                }
            }
        },
        confirmButton = {
            Button(onClick = { onApplyFilter(selectedTag) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

