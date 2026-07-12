@file:OptIn(ExperimentalUuidApi::class)

package kdbrian.github.io.drag.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["username"]),
        UniqueConstraint(columnNames = ["email"]),
    ]
)
class User(
    @Id
    var id: String = Uuid.random().toString().split("-").first(),
    @field:NotBlank
    @Column(nullable = false, unique = true)
    var username: String = "",
    @field:NotBlank
    @field:Email
    @Column(nullable = false, unique = true)
    var email: String = "",
    @JsonIgnore
    @Column(nullable = false)
    var passwordHash: String = "",
    var createdAt: Long = System.currentTimeMillis(),
)
