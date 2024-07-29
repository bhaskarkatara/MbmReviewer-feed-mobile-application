package com.example.mbmkadhumdhadaka.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDetailsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun saveUserDetails(userId: String, name: String, status: String, photoUrl: String, email: String) {
        val userMap = hashMapOf(
            "name" to name,
            "status" to status,
            "photoUrl" to photoUrl,
            "email" to email)
        usersCollection.document(userId).set(userMap).await()
    }

    suspend fun getUserDetails(userId: String): Map<String, Any>? {
        val documentSnapshot = usersCollection.document(userId).get().await()
        return documentSnapshot.data
    }
}