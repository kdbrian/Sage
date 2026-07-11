package kdbrian.github.io.drag.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kdbrian.github.io.drag.data.repo.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.let(jwtService::parseUserId)
            ?.let(userRepository::findById)
            ?.orElse(null)
            ?.let { user ->
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(user, null, emptyList())
            }

        filterChain.doFilter(request, response)
    }
}
