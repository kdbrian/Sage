package io.github.junrdev.sage.domain.model

import java.time.LocalDate

open class AppUser(
    val dateJoined: String = LocalDate.now().toString(),
    val isActive: Boolean = true
)

class AuthenticatedFirebaseUser(
    val userId: String,
    val userName: String,
    val email: String,
    val uploadCount: Long = 0L
) : AppUser()