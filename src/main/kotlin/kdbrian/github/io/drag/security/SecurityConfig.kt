package kdbrian.github.io.drag.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
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
private val API_PUBLIC_PATHS = arrayOf(
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

private val API_MATCHERS = arrayOf(
    "/api/**", "/graphql", "/graphql-ws", "/graph", "/actuator/**", "/scalar/**", "/v3/api-docs/**", "/error",
)

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val apiKeyAuthFilter: ApiKeyAuthFilter,
    private val rateLimitFilter: RateLimitFilter,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    // Token-based clients only (mobile, external JS, curl, GraphiQL). Stateless,
    // no sessions, no CSRF -- nothing here rides a cookie, so nothing here is
    // CSRF-exposed. Left entirely as-is by the JTE login work below: that's a
    // separate chain (see webFilterChain) so existing API clients see zero
    // behavior change.
    @Bean
    @Order(1)
    fun apiFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher(*API_MATCHERS)
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                API_PUBLIC_PATHS.forEach { authorize(it, permitAll) }
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthFilter)
            addFilterAfter<JwtAuthFilter>(apiKeyAuthFilter)
            addFilterAfter<ApiKeyAuthFilter>(rateLimitFilter)
        }
        return http.build()
    }

    // Everything else: the server-rendered JTE app. Ordinary session-cookie
    // login, CSRF left ON (default) since this one *does* ride a cookie.
    // DomainAuthenticationProvider backs the credential check.
    @Bean
    @Order(2)
    fun webFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/login", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin {
                loginPage = "/login"
                defaultSuccessUrl("/", true)
                failureUrl = "/login?error"
            }
            logout {
                logoutUrl = "/logout"
                logoutSuccessUrl = "/login?logout"
                deleteCookies("JSESSIONID")
            }
        }
        return http.build()
    }
}
