package com.example.mbmkadhumdhadaka.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDetailsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun saveUserDetails(userId: String, name: String, status: String, photoUrl: String, email: String
    ) {
        val userMap = hashMapOf(
            "name" to name,
            "status" to status,
            "photoUrl" to photoUrl,
            "email" to email

            )
        usersCollection.document(userId).set(userMap).await()
    }


    suspend fun addPostIdToUser(userId: String, postId: String) {
//        val usersCollection = Firebase.firestore.collection("users")

        try {
            // Fetch user document
            val document = usersCollection.document(userId).get().await()

            if (document.exists()) {
                val postIds = document.get("postIds") as? List<*>
                if (postIds != null) {
                    // Field already exists, add postId
                    usersCollection.document(userId)
                        .update("postIds", FieldValue.arrayUnion(postId))
                        .await()
                } else {
                    // Initialize field with the first postId
                    usersCollection.document(userId)
                        .update("postIds", listOf(postId))
                        .await()
                }
            } else {
                Log.e("Firestore", "User document does not exist")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error updating user document: ${e.message}", e)
        }
    }


    suspend fun getUserDetails(userId: String): Map<String, Any>? {
        val documentSnapshot = usersCollection.document(userId).get().await()
        return documentSnapshot.data
    }
}