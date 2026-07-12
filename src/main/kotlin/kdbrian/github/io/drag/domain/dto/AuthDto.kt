package kdbrian.github.io.drag.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

private const val PASSWORD_POLICY =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,12}\$"

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    val username: String = "",
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Must be a valid email address")
    val email: String = "",
    @field:Pattern(
        regexp = PASSWORD_POLICY,
        message = "Password must be 8-12 characters and include an uppercase letter, a lowercase letter, a number, and a special character",
    )
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
