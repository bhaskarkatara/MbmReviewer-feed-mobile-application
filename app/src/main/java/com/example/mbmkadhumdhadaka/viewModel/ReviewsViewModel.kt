package com.example.mbmkadhumdhadaka.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mbmkadhumdhadaka.dataModel.ReviewModel
import com.example.mbmkadhumdhadaka.repository.ReviewsRepository
import kotlinx.coroutines.launch

class ReviewsViewModel : ViewModel() {

    private val reviewRepository: ReviewsRepository = ReviewsRepository()
    private val _reviewsData = MutableLiveData<List<ReviewModel>>()

    // public var which is exposed to the UI
    val reviewData: LiveData<List<ReviewModel>> get()= _reviewsData

    init {
        loadReviews()
    }

    fun createReviewsFormat(content: String, rating: Int, tag: String) {
        viewModelScope.launch {
            try {
                reviewRepository.createReviewsFormat(content, rating, tag)
                loadReviews()
            } catch (e: Exception) {
//                _reviewsData.value = ResultReviews.Error("Failed to create review: ${e.message}")
            }
        }
    }

    private fun loadReviews() {
//        _reviewsData.value = ResultRevie
        viewModelScope.launch {
            try {
              _reviewsData.value = reviewRepository.getReviews()
            } catch (e: Exception) {
//                _reviewsData.value = ResultReviews.Error("Failed to load data: ${e.message}")
            }
        }
    }
}

sealed class ResultReviews<out T> {
    data class Success<out T>(val data: T) : ResultReviews<T>()
    data class Error(val exception: String) : ResultReviews<Nothing>()
//    object Loading : ResultReviews<Nothing>()
}
