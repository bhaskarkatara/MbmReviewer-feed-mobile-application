package com.example.mbmkadhumdhadaka.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mbmkadhumdhadaka.dataModel.PostModel
import com.example.mbmkadhumdhadaka.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val postRepository: PostRepository = PostRepository()
    private val _postsData = MutableLiveData<PostResult<List<PostModel<Any?>>>>()

    val postsData: LiveData<PostResult<List<PostModel<Any?>>>> get() = _postsData

    init {
        loadPosts()
    }

    fun createPost(post: PostModel<Any?>) {
        _postsData.value = PostResult.Loading
        viewModelScope.launch {
            try {
                postRepository.createPost(post)
                loadPosts()  // Reload posts after creating a new one
            } catch (e: Exception) {
                Log.e(TAG, "createPost: failed to create post: ${e.message}", e)
            }
        }
    }

    fun loadPosts() {
        _postsData.value = PostResult.Loading
        viewModelScope.launch {
            try {
                val posts = postRepository.loadPosts()
                _postsData.value = PostResult.Success(posts)
                Log.d(TAG, "loadPosts: ${_postsData.value}")
            } catch (e: Exception) {
                _postsData.value = PostResult.Error("Failed to load data: ${e.message}")
            }
        }
    }
}

sealed class PostResult<out T> {
    data class Success<out T>(val data: T) : PostResult<T>()
    data class Error(val exception: String) : PostResult<Nothing>()
    object Loading : PostResult<Nothing>()
}
