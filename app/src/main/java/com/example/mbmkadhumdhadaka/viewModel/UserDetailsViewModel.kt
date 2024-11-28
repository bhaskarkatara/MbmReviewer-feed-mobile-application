package com.example.mbmkadhumdhadaka.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mbmkadhumdhadaka.repository.UserDetailsRepository
import kotlinx.coroutines.launch

class UserDetailsViewModel : ViewModel() {

 private val userDetailsRepository = UserDetailsRepository()
  private val _userDetails = MutableLiveData<Map<String, Any>?>()
    val userDetails: MutableLiveData<Map<String, Any>?> = _userDetails
    fun saveUserDetails(userId: String, name: String, status: String, photoUrl: String, email: String, userIds: List<String>) {
        viewModelScope.launch {
            userDetailsRepository.saveUserDetails(userId, name, status, photoUrl, email,userIds)
        }
    }

    fun getUserDetails(userId: String) {
        viewModelScope.launch {
            val userDetails = userDetailsRepository.getUserDetails(userId)
            // Handle the retrieved user details
            _userDetails.value = userDetails
        }

    }
    fun resetCurrentUser(){
        _userDetails.value = null;
    }

}
sealed class UserDetailsState {

    object Loading : UserDetailsState()
    object Success : UserDetailsState()
    data class Error(val exception: String) : UserDetailsState()
}
