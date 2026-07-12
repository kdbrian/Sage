@file:OptIn(ExperimentalUuidApi::class)

package kdbrian.github.io.drag.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
@Table(
    name = "passkey_credentials",
    uniqueConstraints = [UniqueConstraint(columnNames = ["credential_id"])]
)
class PasskeyCredential(
    @Id
    var id: String = Uuid.random().toString().split("-").first(),
    @ManyToOne
    var user: User = User(),
    @Column(name = "credential_id", nullable = false, unique = true, length = 512)
    var credentialId: String = "",
    @Column(nullable = false, length = 1024)
    var publicKeyCose: String = "",
    var signatureCount: Long = 0,
    var transports: String = "",
    var createdAt: Long = System.currentTimeMillis(),
)
