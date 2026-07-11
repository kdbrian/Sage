@file:OptIn(ExperimentalUuidApi::class)

package kdbrian.github.io.drag.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
@Table(
    name = "topic_subscriptions",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "category_id"])]
)
class TopicSubscription(
    @Id
    var id: String = Uuid.random().toString().split("-").first(),
    @ManyToOne
    var user: User = User(),
    @ManyToOne
    var category: DocumentCategory = DocumentCategory(),
    var createdAt: Long = System.currentTimeMillis(),
)
