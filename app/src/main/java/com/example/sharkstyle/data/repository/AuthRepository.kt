package com.example.sharkstyle.data.repository

import com.example.sharkstyle.data.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,

) {
    fun signUp(email: String, password: String): Flow<Resource<AuthResult>> = flow {
        try{
            emit(Resource.Loading())
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
    fun getUser(): String? {
        return auth.currentUser?.email
    }

    fun login(email: String, password: String): Flow<Resource<AuthResult>> = flow{
        try {
            emit(Resource.Loading())
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        } catch (e: Exception){
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser
}