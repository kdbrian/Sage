package kdbrian.github.io.drag.data.impl

import com.yubico.webauthn.AssertionRequest
import com.yubico.webauthn.CredentialRepository
import com.yubico.webauthn.FinishAssertionOptions
import com.yubico.webauthn.FinishRegistrationOptions
import com.yubico.webauthn.RegisteredCredential
import com.yubico.webauthn.RelyingParty
import com.yubico.webauthn.StartAssertionOptions
import com.yubico.webauthn.StartRegistrationOptions
import com.yubico.webauthn.data.PublicKeyCredential
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor
import com.yubico.webauthn.data.RelyingPartyIdentity
import com.yubico.webauthn.data.UserIdentity
import com.yubico.webauthn.data.ByteArray as WebAuthnByteArray
import kdbrian.github.io.drag.data.repo.PasskeyCredentialRepository
import kdbrian.github.io.drag.data.repo.UserRepository
import kdbrian.github.io.drag.domain.model.PasskeyCredential
import kdbrian.github.io.drag.domain.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 * Relying Party for WebAuthn/passkeys, additive to password login. Pending
 * ceremony state (the challenge issued by `start*`) lives in memory keyed by
 * user (registration, since the caller is already signed in) or by the
 * challenge itself (authentication, since the caller isn't identified yet) --
 * fine for a single instance; would need a shared store behind a load
 * balancer.
 */
@Service
class PasskeyService(
    private val passkeyCredentialRepository: PasskeyCredentialRepository,
    private val userRepository: UserRepository,
    @Value("\${webauthn.rp-id}") rpId: String,
    @Value("\${webauthn.rp-name}") rpName: String,
    @Value("\${webauthn.origins}") origins: String,
) : CredentialRepository {

    private val pendingRegistrations = ConcurrentHashMap<String, PublicKeyCredentialCreationOptions>()
    private val pendingAssertions = ConcurrentHashMap<String, AssertionRequest>()

    private val relyingParty: RelyingParty = RelyingParty.builder()
        .identity(RelyingPartyIdentity.builder().id(rpId).name(rpName).build())
        .credentialRepository(this)
        .origins(origins.split(",").map { it.trim() }.toSet())
        .build()

    fun startRegistration(user: User): String {
        val options = relyingParty.startRegistration(
            StartRegistrationOptions.builder()
                .user(
                    UserIdentity.builder()
                        .name(user.username)
                        .displayName(user.username)
                        .id(userHandle(user))
                        .build()
                )
                .build()
        )
        pendingRegistrations[user.id] = options
        return options.toCredentialsCreateJson()
    }

    fun finishRegistration(user: User, credentialResponseJson: String): PasskeyCredential {
        val options = pendingRegistrations.remove(user.id)
            ?: throw IllegalStateException("No passkey registration in progress for this user")
        val credential = PublicKeyCredential.parseRegistrationResponseJson(credentialResponseJson)
        val result = relyingParty.finishRegistration(
            FinishRegistrationOptions.builder().request(options).response(credential).build()
        )
        return passkeyCredentialRepository.save(
            PasskeyCredential(
                user = user,
                credentialId = result.keyId.id.base64Url,
                publicKeyCose = result.publicKeyCose.base64Url,
                signatureCount = result.signatureCount,
                transports = result.keyId.transports.orElse(sortedSetOf()).joinToString(",") { it.id },
            )
        )
    }

    fun startAuthentication(usernameOrEmail: String?): String {
        val builder = StartAssertionOptions.builder()
        usernameOrEmail?.let { builder.username(it) }
        val request = relyingParty.startAssertion(builder.build())
        pendingAssertions[request.publicKeyCredentialRequestOptions.challenge.base64Url] = request
        return request.toCredentialsGetJson()
    }

    fun finishAuthentication(credentialResponseJson: String): User {
        val credential = PublicKeyCredential.parseAssertionResponseJson(credentialResponseJson)
        val challengeKey = credential.response.clientData.challenge.base64Url
        val request = pendingAssertions.remove(challengeKey)
            ?: throw IllegalStateException("No passkey authentication in progress")
        val result = relyingParty.finishAssertion(
            FinishAssertionOptions.builder().request(request).response(credential).build()
        )
        if (!result.isSuccess) throw IllegalStateException("Passkey authentication failed")
        return userRepository.findByUsername(result.username)
            ?: throw IllegalStateException("Passkey credential is not linked to a known user")
    }

    private fun userHandle(user: User) = WebAuthnByteArray(user.id.toByteArray())

    // CredentialRepository -- backs the RelyingParty's own lookups during ceremonies.

    override fun getCredentialIdsForUsername(username: String): Set<PublicKeyCredentialDescriptor> {
        val user = userRepository.findByUsername(username) ?: return emptySet()
        return passkeyCredentialRepository.findByUser_Id(user.id)
            .map { PublicKeyCredentialDescriptor.builder().id(WebAuthnByteArray.fromBase64Url(it.credentialId)).build() }
            .toSet()
    }

    override fun getUserHandleForUsername(username: String): Optional<WebAuthnByteArray> =
        Optional.ofNullable(userRepository.findByUsername(username)?.let { userHandle(it) })

    override fun getUsernameForUserHandle(userHandle: WebAuthnByteArray): Optional<String> =
        userRepository.findById(String(userHandle.bytes)).map { it.username }

    override fun lookup(credentialId: WebAuthnByteArray, userHandle: WebAuthnByteArray): Optional<RegisteredCredential> =
        Optional.ofNullable(passkeyCredentialRepository.findByCredentialId(credentialId.base64Url)?.let { toRegisteredCredential(it) })

    override fun lookupAll(credentialId: WebAuthnByteArray): Set<RegisteredCredential> =
        passkeyCredentialRepository.findByCredentialId(credentialId.base64Url)?.let { setOf(toRegisteredCredential(it)) } ?: emptySet()

    private fun toRegisteredCredential(c: PasskeyCredential): RegisteredCredential =
        RegisteredCredential.builder()
            .credentialId(WebAuthnByteArray.fromBase64Url(c.credentialId))
            .userHandle(userHandle(c.user))
            .publicKeyCose(WebAuthnByteArray.fromBase64Url(c.publicKeyCose))
            .signatureCount(c.signatureCount)
            .build()
}
