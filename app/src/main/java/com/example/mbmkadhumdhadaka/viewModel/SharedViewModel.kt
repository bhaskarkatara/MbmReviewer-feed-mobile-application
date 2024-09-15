package com.example.mbmkadhumdhadaka.viewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class SharedViewModel : ViewModel(){
    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl: MutableLiveData<String?> get() = _imageUrl

    fun setImageUrl(url: String) {
        _imageUrl.value = url
        Log.d("SharedViewModel", "Image URL set to: $url")
    }
}


