package io.github.junrdev.sage.domain.repo

interface FirebaseUserRepo {

    suspend fun registerUserWithEmailPassword(
        email: String,
        password: String
    ): Result<String>

    suspend fun loginUserWithEmailPassword(
        email: String,
        password: String
    ): Result<String>

    suspend fun resetPasswordWithEmail(email: String): Result<String>
    suspend fun verifyUserEmail(email: String): Result<String>
    suspend fun logoutUser()

}