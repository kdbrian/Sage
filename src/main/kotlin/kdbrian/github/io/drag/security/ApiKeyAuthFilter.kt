package kdbrian.github.io.drag.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/** Principal marker for requests authenticated via the server-maintained GraphQL API key. */
const val API_KEY_PRINCIPAL = "api-key"

/**
 * Lets a fixed, server-maintained key unlock the /graphql endpoint without a
 * user login, so GraphiQL's schema introspection works for anyone browsing
 * the docs. ApiKeyIntrospectionInterceptor is what actually stops this from
 * being used for real data access -- this filter only marks the request as
 * authenticated at all, same as any other login would.
 */
@Component
class ApiKeyAuthFilter(
    @Value("\${graphql.api-key}") private val apiKey: String,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (SecurityContextHolder.getContext().authentication == null && request.getHeader("X-Api-Key") == apiKey) {
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                API_KEY_PRINCIPAL, null, listOf(SimpleGrantedAuthority("ROLE_INTROSPECTION"))
            )
        }
        filterChain.doFilter(request, response)
    }
}
