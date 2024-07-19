package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mbmkadhumdhadaka.Screens
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@Composable

fun FeedScreen(navController: NavController,authViewModel: AuthViewModel) {
    Column {
    Box (modifier = Modifier.fillMaxSize()){
        FloatingActionButton(
            onClick = { /* TODO: Handle FAB click */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp) // Adjust the padding to fit your design
        ) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = "Create post"
            )
        }
    }
    }

    }



