package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.util.appendPlaceholders
import com.example.mbmkadhumdhadaka.R
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Composable
fun CreatePost(navController: NavController, authViewModel: AuthViewModel) {
    var postContent by remember { mutableStateOf("") }
    var isShowMedia by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create post") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    OutlinedButton(onClick = { /*TODO*/ },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = if(postContent.isNotEmpty())Color.Red else Color.Transparent)) {
                        Text(text = "Post")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp) // Add any additional padding if needed
            , horizontalAlignment = Alignment.Start 
        ) {

          
            Row {
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("your_name", style = MaterialTheme.typography.h6)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier  //Box allows you to stack multiple children on top of each other
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.1f), RectangleShape)
                .border(1.dp, Color.Gray, RectangleShape)
                .padding(16.dp)) {
                BasicTextField(
                    value = postContent,
                    onValueChange = { postContent = it },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                )
                if (postContent.isEmpty()) {
                    Text(
                        text = "What's in your Mind",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        textAlign = TextAlign.Start
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            IconButton(onClick = { isShowMedia = !isShowMedia}) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "pickers")
            }
            if(isShowMedia){
                Text(text = "Photo", modifier = Modifier.clickable {
                    //todo: add logic
                })
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Video", modifier = Modifier.clickable {
                    //todo: add logic
                })

            }

            
        }
    }
}
