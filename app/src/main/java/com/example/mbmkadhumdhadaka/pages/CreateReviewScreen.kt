package com.example.mbmkadhumdhadaka.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import com.example.mbmkadhumdhadaka.viewModel.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReviewScreen(navController: NavController, reviewsViewModel: ReviewsViewModel) {
    var reviewText by remember { mutableStateOf("") }
    var selectedRating by remember { mutableIntStateOf(1) }
    var selectedTag by remember { mutableStateOf("Mess") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "आपकी विचार /Reviews",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Content")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // Set height to display 3-4 lines
                        .verticalScroll(scrollState) // Enable scrolling
                        .padding(4.dp) // Optional padding inside the box
                ) {
                    TextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        maxLines = 3 // Maximum lines to show
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Rating")
                Row {
                    (1..5).forEach { rating ->
                        RadioButton(
                            selected = selectedRating == rating,
                            onClick = { selectedRating = rating }
                        )
                        Text(text = rating.toString(), modifier = Modifier.padding(1.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Tag")
                TextField(
                    value = selectedTag,
                    label = { Text(text = "Library, Hostel, MBM., etc.") },
                    onValueChange = { selectedTag = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // TODO: Save Review
                    if(reviewText.isEmpty() || selectedTag.isEmpty() || selectedRating == 0){
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                      reviewsViewModel.createReviewsFormat(reviewText,rating = selectedRating,tag = selectedTag)
                    Toast.makeText(context, "Submit Review", Toast.LENGTH_SHORT).show()
                    // Reset state after showing Toast
                    reviewText = ""
                    selectedRating = 1
                    selectedTag = "Mess"
                    navController.navigate("review_screen")
                }) {
                    Text(text = "Submit Review")
                }
            }
        }
    }
}
