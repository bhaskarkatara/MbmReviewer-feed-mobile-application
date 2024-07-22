package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import com.example.mbmkadhumdhadaka.Screens
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@Composable
fun FeedScreen(navController: NavController, authViewModel: AuthViewModel) {
    var isMenuExpanded by remember { mutableStateOf(false) }

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
                    onClick = { /* TODO: Handle first menu item click */ },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "rate this app")
                }
                FloatingActionButton(
                    onClick = { navController.navigate("create_post_screen") },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "create Post")
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
