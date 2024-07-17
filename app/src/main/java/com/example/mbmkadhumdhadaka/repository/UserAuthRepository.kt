package com.example.mbmkadhumdhadaka.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserAuthRepository (private val auth: FirebaseAuth,
                          private val firestore: FirebaseFirestore){

}