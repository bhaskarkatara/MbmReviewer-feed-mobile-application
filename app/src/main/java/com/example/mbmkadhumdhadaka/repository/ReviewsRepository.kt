package com.example.mbmkadhumdhadaka.repository

import com.example.mbmkadhumdhadaka.dataModel.ReviewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewsRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()){


     suspend fun createReviewsFormat(content: String, rating: Int, tag: String){
         val review = ReviewModel(content,rating,tag)
         firestore.collection("reviews").add(review).await()
     }
    // store feedback into firebase here..
    suspend fun createFeedback(content: String){
        val feedback = FeedbackModel(content)
        firestore.collection("feedback").add(feedback).await()
    }

    suspend fun getReviews(): List<ReviewModel> {
        val snapshot = firestore.collection("reviews").get().await()
        return snapshot.toObjects(ReviewModel::class.java)
    }
}

data class FeedbackModel(val content: String)