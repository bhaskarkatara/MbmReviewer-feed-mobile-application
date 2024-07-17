package com.example.mbmkadhumdhadaka

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LgSpScreen(navController: NavController){
    val registerClick = remember {
        mutableStateOf(true)
    }
    val loginClick = remember {
        mutableStateOf(false)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }

//    val cartoonOffsetX by animateFloatAsState(
//        targetValue = if (registerClick.value || loginClick.value) 360f else 0f,
//        animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""
//    )


    Column (modifier = Modifier.padding(50.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center){
            OutlinedButton(onClick = {
                registerClick.value = true
                loginClick.value = false
            }) {
                Text(text = "Register")

            }
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedButton(onClick = {
                loginClick.value= true
                registerClick.value = false
            }) {
                Text(text = "Login")

            }
        }
        Text(text = "एमबीएम वर्ल्ड में आपका स्वागत है", modifier = Modifier.padding(bottom = 16.dp))
        if(registerClick.value){
            ShowRegisterForm()
        }
        if(loginClick.value){
            ShowLoginForm()
        }
//        AnimatedCartoon(offsetX = cartoonOffsetX)
        // terms & conditions
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp), // Adjust horizontal padding as needed
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Spacer to push content to the top

            // Clickable Text
            Text(
                text = "Terms & Conditions",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    showDialog.value = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between text and dialog

            // Dialog
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    confirmButton = {
                        Button(onClick = { showDialog.value = false }) {
                            Text("OK")
                        }
                    },
                    text = {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "धन्यवाद एमबीएम के सस्ते लोगों, इस ऐप का उद्देश्य केवल मजाक बनाना है," +
                                    " किसी की भावनाओं का अनादर करने का कोई इरादा नहीं है।                                 " +
                                    " *आपकी पहचान पूरी तरह छुप जाएगी* " +

                                    " 1.यहां आप अपने डार्क मीम्स शेयर कर सकते हैं\n" +
                                    "2. आप एमबीएम हॉस्टल, मेस, विभागों के बारे में समीक्षा और प्रतिक्रिया साझा कर सकते हैं।                                    " +
                                    "धन्यवाद, मुगनीराम जी की ओर से प्यार :)"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ShowRegisterForm() {
    var onNameChanged by remember { mutableStateOf("") }
    var onEmailChange by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = onNameChanged,
            onValueChange = { onNameChanged = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना नाम दर्ज करें") }
        )

        OutlinedTextField(
            value = onEmailChange,
            onValueChange = { onEmailChange = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना ईमेल आईडी दर्ज करें") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना कूटशब्द(pass..) भरें") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text(text = "Register Now")
        }
    }
}
@Composable
fun ShowLoginForm() {
    var onEmailChange by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = onEmailChange,
            onValueChange = { onEmailChange = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना ईमेल आईडी दर्ज करें") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना कूटशब्द(pass..) भरें") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text(text = "LoginToMbm")
        }
    }
}
//@Composable
//fun AnimatedCartoon(offsetX: Float) {
//    Box(
//        modifier = Modifier
//            .offset(x = offsetX.dp)
//            .height(100.dp)
//            .width(100.dp)
//    ) {
//        Text(
//            text = "🐱", // Replace with your animated cartoon asset
//            fontSize = 40.sp
//        )
//    }
//}
