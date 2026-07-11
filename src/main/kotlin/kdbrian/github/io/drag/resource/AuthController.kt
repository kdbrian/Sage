package kdbrian.github.io.drag.resource

import kdbrian.github.io.drag.data.impl.AuthService
import kdbrian.github.io.drag.domain.dto.AuthResponse
import kdbrian.github.io.drag.domain.dto.LoginRequest
import kdbrian.github.io.drag.domain.dto.RegisterRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): AuthResponse = authService.register(request)

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): AuthResponse = authService.login(request)
}
