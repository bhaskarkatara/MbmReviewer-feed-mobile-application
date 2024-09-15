package com.example.mbmkadhumdhadaka.repository

import com.example.mbmkadhumdhadaka.dataModel.PostModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class PostRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
                     private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    ) {

    // Create a new post with a unique ID
    suspend fun createPost(post: PostModel<Any?>) {
        try {
            // Generate a unique post ID using UUID
            val postId = UUID.randomUUID().toString()
            post.postId = postId  // Ensure postId is set in the post model

            // Add the post to the 'posts' collection
            firestore.collection("posts").document(postId).set(post).await()
        } catch (e: Exception) {
            // Handle any errors that occur during post creation
            throw e  // You can log this or show a UI message in your app
        }
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
