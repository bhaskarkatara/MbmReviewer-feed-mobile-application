package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Composable
fun CreatePost(navController: NavController, authViewModel: AuthViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFFFFC0CB) // Custom color for the background
//                )
            )
        }
    ) { innerPadding ->
        // Content of CreatePost screen goes here
        // Adjust Modifier.padding(innerPadding) for correct padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp) // Add any additional padding if needed
        ) {
            // Your form components or content here
          //  Text(text = "Create your post here", style = MaterialTheme.typography.bodyLarge)

            // Example of a text field
            // TextField(
            //     value = "Some Text",
            //     onValueChange = { /* Handle text change */ },
            //     label = { Text("Post Title") }
            // )

        }
    }
}
