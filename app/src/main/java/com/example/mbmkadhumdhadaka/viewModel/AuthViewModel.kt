package com.example.mbmkadhumdhadaka.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        _authState.value = if (auth.currentUser != null && auth.currentUser!!.isEmailVerified) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        if (user != null) {
                            startEmailVerificationCheck(user)
                            _authState.value = AuthState.EmailVerificationPending
                        }
//                        _authState.value = AuthState.Error("Please verify your email first")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
        }
    }

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                _authState.value = AuthState.EmailVerificationSent
                            } else {
                                _authState.value = AuthState.Error(
                                    verificationTask.exception?.message
                                        ?: "Failed to send verification email"
                                )
                            }
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
        }
    }
    private fun startEmailVerificationCheck(user: FirebaseUser) {
        viewModelScope.launch {
            while (!user.isEmailVerified) {
                delay(5000) // Check every 5 seconds
                user.reload().addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful) {
                        if (user.isEmailVerified) {
                            _authState.value = AuthState.Authenticated
                        }
                    }
                }
            }
        }
    }


    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetError() {
        _authState.value = null
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object EmailVerificationSent : AuthState()
    object EmailVerificationPending : AuthState()
    data class Error(val exception: String) : AuthState()
}
