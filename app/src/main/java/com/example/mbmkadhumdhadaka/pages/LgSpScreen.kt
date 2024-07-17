package com.example.mbmkadhumdhadaka.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mbmkadhumdhadaka.viewModel.AuthState
import com.example.mbmkadhumdhadaka.viewModel.AuthViewModel

@Composable
fun LgSpScreen(navController: NavController,authViewModel: AuthViewModel){
    val registerClick = remember {
        mutableStateOf(true)
    }
    val loginClick = remember {
        mutableStateOf(false)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    val emailLinkClick = remember { mutableStateOf(false) }
 val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect (authState.value){
          when(authState.value){
              is AuthState.Authenticated ->{
                  navController.navigate("feed_screen")
              }
              is AuthState.Error ->{
                  Toast.makeText(context,(authState.value as AuthState.Error).exception, Toast.LENGTH_SHORT).show()
                  authViewModel.resetError()
              }
              else -> Unit
          }
    }


    Column (modifier = Modifier.padding(50.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center){
            OutlinedButton(onClick = {
                registerClick.value = true
                loginClick.value = false
                emailLinkClick.value = false
            }) {
                Text(text = "Register")

            }
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedButton(onClick = {
                loginClick.value= true
                registerClick.value = false
                emailLinkClick.value = false
            }) {
                Text(text = "Login")

            }
        }
        Text(text = "एमबीएम वर्ल्ड में आपका स्वागत है", modifier = Modifier.padding(bottom = 16.dp))
        when (authState.value) {
            is AuthState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            else -> {
                when {
                    registerClick.value -> ShowRegisterForm(authViewModel, navController)
                    loginClick.value -> ShowLoginForm(authViewModel, navController)
                    emailLinkClick.value -> ShowEmailLinkForm(authViewModel)
                }
            }
        }

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
fun ShowEmailLinkForm(authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "Enter your email address") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        Button(
            onClick = {
                val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("email", email).apply()
                authViewModel.sendSignInLink(email)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Send Sign-In Link")
        }
    }
}
@Composable
fun ShowRegisterForm(authViewModel: AuthViewModel,navController: NavController) {
    var onNameChanged by remember { mutableStateOf("") }
    var onEmailChange by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect (authState.value){
        when(authState.value){
            is AuthState.Authenticated ->{
                navController.navigate("feed_screen")
            }
            is AuthState.Error ->{
                Toast.makeText(context,(authState.value as AuthState.Error).exception, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = onNameChanged,
            onValueChange = { onNameChanged = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना नाम दर्ज करें") }
        )

        OutlinedTextField(
            value = onEmailChange,
            singleLine = true,
            onValueChange = { onEmailChange = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना ईमेल आईडी दर्ज करें") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            singleLine = true,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना कूटशब्द(pass..) भरें") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedButton(onClick = { authViewModel.signUp(onEmailChange,password) }) {
            Text(text = "Register Now")
        }
    }
}

// LOGIN FORM
@Composable
fun ShowLoginForm(authViewModel: AuthViewModel,navController: NavController) {
    var onEmailChange by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect (authState.value){
        when(authState.value){
            is AuthState.Authenticated ->{
                navController.navigate("feed_screen")
            }
            is AuthState.Error ->{
                Toast.makeText(context,(authState.value as AuthState.Error).exception, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = onEmailChange,
            onValueChange = { onEmailChange = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना ईमेल आईडी दर्ज करें") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = "अपना कूटशब्द(pass..) भरें") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedButton(onClick = { authViewModel.login(email = onEmailChange,password = password) }) {
            Text(text = "LoginToMbm")
        }
    }
}
