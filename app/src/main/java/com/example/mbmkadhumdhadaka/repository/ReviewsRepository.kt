package com.example.mbmkadhumdhadaka.repository

import com.example.mbmkadhumdhadaka.dataModel.ReviewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewsRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()){


     suspend fun createReviewsFormat(content: String, rating: Int, tag: String){
         val review = ReviewModel(content,rating,tag)
         firestore.collection("reviews").add(review).await()
     }

    suspend fun getReviews(): List<ReviewModel> {
        val snapshot = firestore.collection("reviews").get().await()
        return snapshot.toObjects(ReviewModel::class.java)
    }
}