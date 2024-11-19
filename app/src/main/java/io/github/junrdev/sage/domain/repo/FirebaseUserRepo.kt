package io.github.junrdev.sage.domain.repo

interface FirebaseUserRepo {

    suspend fun registerUserWithEmailPassword(
        email: String,
        password: String,
        onResult: (Result<String>) -> Unit
    )

    suspend fun loginUserWithEmailPassword(
        email: String,
        password: String,
        onResult: (Result<String>) -> Unit
    )

    suspend fun resetPasswordWithEmail(email: String, onResult: (Result<String>) -> Unit)
    suspend fun verifyUserEmail(email: String, onResult: (Result<String>) -> Unit)

}