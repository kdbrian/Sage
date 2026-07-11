@file:OptIn(ExperimentalUuidApi::class)

package kdbrian.github.io.drag.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import kdbrian.github.io.drag.domain.enums.NotificationType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
@Table(name = "notifications")
class Notification(
    @Id
    var id: String = Uuid.random().toString().split("-").first(),
    @ManyToOne
    var user: User = User(),
    var documentId: String = "",
    var documentTitle: String = "",
    var categoryId: String = "",
    var categoryName: String = "",
    @Enumerated(EnumType.STRING)
    var type: NotificationType = NotificationType.UPLOADED,
    var message: String = "",
    var read: Boolean = false,
    var createdAt: Long = System.currentTimeMillis(),
)
