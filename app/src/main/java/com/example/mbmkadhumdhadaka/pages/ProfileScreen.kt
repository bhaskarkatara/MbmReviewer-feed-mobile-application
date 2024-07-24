package com.example.mbmkadhumdhadaka.pages

//package com.example.mbmkadhumdhadaka.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import com.example.mbmkadhumdhadaka.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Edit Profile", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                var name by remember { mutableStateOf("") }
                var status by remember { mutableStateOf("") }
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text(text = "Status") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                        Text(text = "Cancel", modifier = Modifier.clickable { scope.launch { sheetState.collapse() } })
                    Spacer(modifier = Modifier.width(20.dp))

                        Text(text = "Save", modifier = Modifier.clickable { scope.launch { sheetState.collapse() } })

                }
            }
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile", modifier = Modifier.padding(start = 10.dp))

                        },
                actions = {
                    IconButton(onClick = {
                        if(sheetState.isCollapsed){
                        scope.launch {
                            sheetState.expand()
                        }
                        } else{
                            scope.launch {
                                sheetState.collapse()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { /* Handle profile picture click */ },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Name Row

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp), horizontalAlignment = Alignment.Start) {
                Row(
                    verticalAlignment = Alignment.Top,
                )
                {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "account")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Name : ",style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "your_name") // here should come the name which is enter by the user

                }



                Spacer(modifier = Modifier.height(16.dp))

                // Status Row
                Row(
                    verticalAlignment = Alignment.Top,
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "status")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Status : " ,style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "your_status") // here should come the status which is enter by the user
                }
            }
            Spacer(modifier = Modifier.height(200.dp))
            Text(text = "--Mugneeram Ji--", style = MaterialTheme.typography.subtitle1 ,color = Color.Magenta)
        }
    }



//    val authState = authViewModel.authState.observeAsState()
//    LaunchedEffect(authState.value) {
//        when (authState.value) {
//            is AuthState.Unauthenticated -> {
//                navController.navigate(Screens.LgSpScreen.route)
//            }
//            else -> Unit
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(top = 70.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Button(onClick = { authViewModel.signOut() }) {
//            Text(text = "logOut")
//        }
//    }

}
