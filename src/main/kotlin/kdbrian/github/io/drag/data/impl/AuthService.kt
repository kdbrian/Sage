package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.data.repo.UserRepository
import kdbrian.github.io.drag.domain.dto.AuthResponse
import kdbrian.github.io.drag.domain.dto.LoginRequest
import kdbrian.github.io.drag.domain.dto.RegisterRequest
import kdbrian.github.io.drag.domain.model.User
import kdbrian.github.io.drag.security.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) {

    fun register(request: RegisterRequest): AuthResponse {
        require(!userRepository.existsByUsername(request.username)) { "Username already taken" }
        require(!userRepository.existsByEmail(request.email)) { "Email already registered" }

        val user = userRepository.save(
            User(
                username = request.username,
                email = request.email,
                passwordHash = passwordEncoder.encode(request.password)!!,
            )
        )
        return user.toAuthResponse()
    }

    fun login(request: LoginRequest): AuthResponse = authenticate(request.usernameOrEmail, request.password).toAuthResponse()

    /** Shared by the JSON login endpoint and the JTE app's session-based form login. */
    fun authenticate(usernameOrEmail: String, password: String): User =
        (userRepository.findByUsername(usernameOrEmail) ?: userRepository.findByEmail(usernameOrEmail))
            ?.takeIf { passwordEncoder.matches(password, it.passwordHash) }
            ?: throw IllegalArgumentException("Invalid credentials")

    private fun User.toAuthResponse() = AuthResponse(
        token = jwtService.generate(id, username),
        userId = id,
        username = username,
    )
}
