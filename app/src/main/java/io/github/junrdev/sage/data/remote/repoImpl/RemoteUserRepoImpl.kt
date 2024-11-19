package io.github.junrdev.sage.data.remote.repoImpl

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.junrdev.sage.domain.repo.FirebaseUserRepo

class FirebaseUserRepoImpl : FirebaseUserRepo {

    private val auth = Firebase.auth

    override suspend fun registerUserWithEmailPassword(
        email: String,
        password: String,
        onResult: (Result<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (it.user != null) {
                    onResult(Result.success("User #${it.user?.uid} created proceed to login"))
                }
            }.addOnFailureListener {
                onResult(Result.failure(it))
            }
    }

    override suspend fun loginUserWithEmailPassword(
        email: String,
        password: String,
        onResult: (Result<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (it.user != null) {
                    onResult(Result.success("${it.user?.uid}"))
                }
            }.addOnFailureListener {
                onResult(Result.failure(it))
            }
    }

    override suspend fun resetPasswordWithEmail(email: String, onResult: (Result<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onResult(Result.success("Check email to proceed."))
            }.addOnFailureListener {
                onResult(Result.failure(it))
            }
    }

    override suspend fun verifyUserEmail(email: String, onResult: (Result<String>) -> Unit) {
        if (auth.currentUser != null) {
            auth.currentUser!!.sendEmailVerification()
                .addOnSuccessListener {
                    onResult(Result.success("Email sent check inbox."))
                }
                .addOnFailureListener {
                    onResult(Result.failure(it))
                }
        } else
            onResult(Result.failure(Exception("Please login first.")))
    }
}