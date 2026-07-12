package kdbrian.github.io.drag.resource

import jakarta.servlet.http.HttpServletRequest
import kdbrian.github.io.drag.data.impl.PasskeyService
import kdbrian.github.io.drag.domain.dto.AuthResponse
import kdbrian.github.io.drag.security.JwtService
import kdbrian.github.io.drag.security.requireCurrentUser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class PasskeyAuthOptionsRequest(val usernameOrEmail: String? = null)

/**
 * WebAuthn ceremony endpoints, additive to password login (see AuthController).
 * Registration (adding a passkey) requires an already-authenticated user;
 * authentication (logging in with one) is necessarily anonymous.
 *
 * The two "options" endpoints return the Yubico library's own JSON verbatim
 * -- clients pass it straight to navigator.credentials.create()/.get() (or
 * the platform equivalent on mobile) under its "publicKey" key. The two
 * "finish" endpoints expect the raw JSON the authenticator's response
 * serializes to (credential.toJSON() client-side), read as-is off the
 * request body rather than through a typed DTO, since WebAuthn's binary
 * fields need the library's own decoder, not Jackson's.
 */
@RestController
@RequestMapping("/api/auth/passkey")
class PasskeyController(
    private val passkeyService: PasskeyService,
    private val jwtService: JwtService,
) {

    @PostMapping("/register/options")
    fun registerOptions(): ResponseEntity<String> =
        ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(passkeyService.startRegistration(requireCurrentUser()))

    @PostMapping("/register/finish")
    fun registerFinish(request: HttpServletRequest) {
        passkeyService.finishRegistration(requireCurrentUser(), request.reader.readText())
    }

    @PostMapping("/authenticate/options")
    fun authenticateOptions(@RequestBody(required = false) body: PasskeyAuthOptionsRequest?): ResponseEntity<String> =
        ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(passkeyService.startAuthentication(body?.usernameOrEmail))

    @PostMapping("/authenticate/finish")
    fun authenticateFinish(request: HttpServletRequest): AuthResponse {
        val user = passkeyService.finishAuthentication(request.reader.readText())
        return AuthResponse(token = jwtService.generate(user.id, user.username), userId = user.id, username = user.username)
    }
}
