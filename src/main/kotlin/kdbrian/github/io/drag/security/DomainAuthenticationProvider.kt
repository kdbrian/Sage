package kdbrian.github.io.drag.security

import kdbrian.github.io.drag.data.impl.AuthService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

/**
 * Backs the JTE app's session-based form login. Reuses AuthService's credential
 * check and sets the domain User as the principal, same as JwtAuthFilter does for
 * API calls, so currentUser() works identically regardless of how a request got in.
 */
@Component
class DomainAuthenticationProvider(
    private val authService: AuthService,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val user = runCatching {
            authService.authenticate(authentication.name, authentication.credentials?.toString().orEmpty())
        }.getOrElse { throw BadCredentialsException("Invalid username or password", it) }
        return UsernamePasswordAuthenticationToken(user, null, emptyList())
    }

    override fun supports(authentication: Class<*>): Boolean =
        UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}
