package com.example.mbmkadhumdhadaka.repository

import com.example.mbmkadhumdhadaka.dataModel.PostModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PostRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    suspend fun createPost(post: PostModel<Any?>) {
        firestore.collection("posts").add(post)
    }

    suspend fun loadPosts(): List<PostModel<Any?>> {
        val querySnapshot = firestore.collection("posts").get().await()
        return querySnapshot.documents.mapNotNull { document ->
            val data = document.data
            data?.let {
                // Convert the map to PostModel
                PostModel(
                    postId = document.id,
                    postContent = it["postContent"] as? String ?: "",
                    postImage = it["postImage"] as? String ?: "",
                    postOwnerPhoto = it["postOwnerPhoto"] as? String ?: "",
                    postOwnerName = it["postOwnerName"] as? String ?: "Your Name",
                    userId = it["userId"] as? String ?: "1",
                    timestamp = it["timestamp"] as? Long ?: System.currentTimeMillis()
                )
            }
        }
    }
}
