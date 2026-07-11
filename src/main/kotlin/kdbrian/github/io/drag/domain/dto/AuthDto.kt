package kdbrian.github.io.drag.domain.dto

data class RegisterRequest(
    val username: String = "",
    val email: String = "",
    val password: String = "",
)

data class LoginRequest(
    val usernameOrEmail: String = "",
    val password: String = "",
)

data class AuthResponse(
    val token: String = "",
    val userId: String = "",
    val username: String = "",
)
