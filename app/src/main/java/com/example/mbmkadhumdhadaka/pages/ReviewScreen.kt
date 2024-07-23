package com.example.mbmkadhumdhadaka.pages

//import androidx.compose.material.icons.filled.FilterList
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
//import androidx.compose.runtime.s
import com.example.mbmkadhumdhadaka.dataModel.ReviewModel
import com.example.mbmkadhumdhadaka.viewModel.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(navController: NavController, reviewsViewModel: ReviewsViewModel) {
    val reviews by reviewsViewModel.reviewData.observeAsState(emptyList())

    var filteredReviews by remember { mutableStateOf(reviews) }
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(reviews) {
        filteredReviews = reviews
    }

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
                 reviews
                } else {
                   reviews.filter {it.tagPlaces == tag}
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

