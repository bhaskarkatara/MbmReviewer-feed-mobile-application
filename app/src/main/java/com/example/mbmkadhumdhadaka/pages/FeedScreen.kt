package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mbmkadhumdhadaka.Screens
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@Composable

fun FeedScreen(navController: NavController,authViewModel: AuthViewModel){

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.Unauthenticated ->{
                navController.navigate(Screens.LgSpScreen.route)
            }
            else -> Unit
        }
    }

    Column (modifier = Modifier.fillMaxSize().padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        Text(text = "hii feed screen")
        Button(onClick = { authViewModel.signOut() }) {
            Text(text = "signout")
        }
    }

}