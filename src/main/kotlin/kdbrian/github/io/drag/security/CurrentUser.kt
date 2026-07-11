package kdbrian.github.io.drag.security

import kdbrian.github.io.drag.domain.model.User
import org.springframework.security.core.context.SecurityContextHolder

val currentUser: User?
    get() = SecurityContextHolder.getContext().authentication?.principal as? User

fun requireCurrentUser(): User =
    currentUser ?: throw IllegalStateException("No authenticated user in context")
