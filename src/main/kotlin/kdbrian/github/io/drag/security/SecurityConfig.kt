package kdbrian.github.io.drag.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

// Sign-on + API documentation surfaces only. No document/topic/notification/
// report data lives behind these paths.
//   - /graphql (live query/mutation execution) is deliberately NOT here: it
//     serves the same data the REST endpoints do, so it requires a token
//     like everything else. The /graph explorer shell still loads for
//     anyone; running a query against /graphql needs a bearer token pasted
//     into its Headers panel.
//   - /graphql-ws (subscription socket) stays open at the handshake level
//     only because browsers can't set custom headers on a WS upgrade;
//     GraphQlAuthInterceptor authenticates the connection_init payload
//     instead, and the notifications subscription yields nothing without a
//     valid token, so no data is exposed.
private val PUBLIC_PATHS = arrayOf(
    "/api/auth/register",
    "/api/auth/login",
    // Passkey *authentication* is necessarily anonymous (that's the whole
    // point). Passkey *registration* (/api/auth/passkey/register/**) is
    // deliberately NOT here -- adding a passkey to an account requires
    // already being signed into that account.
    "/api/auth/passkey/authenticate/**",
    "/graph",
    "/graphql-ws",
    "/actuator/**",
    "/scalar/**",
    "/v3/api-docs/**",
    "/error",
)

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val apiKeyAuthFilter: ApiKeyAuthFilter,
    private val rateLimitFilter: RateLimitFilter,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                PUBLIC_PATHS.forEach { authorize(it, permitAll) }
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthFilter)
            addFilterAfter<JwtAuthFilter>(apiKeyAuthFilter)
            addFilterAfter<ApiKeyAuthFilter>(rateLimitFilter)
        }
        return http.build()
    }
}
