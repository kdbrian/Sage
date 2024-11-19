package io.github.junrdev.sage.data.remote.repoImpl

import com.google.firebase.auth.FirebaseAuth
import io.github.junrdev.sage.domain.repo.FirebaseUserRepo
import kotlinx.coroutines.tasks.await

class FirebaseUserRepoImpl(
    private val auth: FirebaseAuth
) : FirebaseUserRepo {

    override suspend fun registerUserWithEmailPassword(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success("User #${authResult.user?.uid} created proceed to login")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUserWithEmailPassword(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                Result.success("${authResult.user?.uid}")
            } else
                Result.failure(Exception("Something unexpected happened!"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPasswordWithEmail(email: String): Result<String> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success("Check email to proceed.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyUserEmail(email: String): Result<String> {
        return try {
            if (auth.currentUser != null) {
                auth.currentUser!!.sendEmailVerification().await()
                Result.success("Email sent check inbox.")
            } else
                Result.failure(Exception("Please login first."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logoutUser() {
        auth.signOut()
    }
}