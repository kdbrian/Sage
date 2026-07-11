@file:OptIn(ExperimentalUuidApi::class)

package kdbrian.github.io.drag.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
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
    @Column(nullable = false, unique = true)
    var username: String = "",
    @Column(nullable = false, unique = true)
    var email: String = "",
    @JsonIgnore
    @Column(nullable = false)
    var passwordHash: String = "",
    var createdAt: Long = System.currentTimeMillis(),
)
