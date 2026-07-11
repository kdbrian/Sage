package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.data.pub.NotificationBroadcaster
import kdbrian.github.io.drag.data.repo.NotificationRepository
import kdbrian.github.io.drag.domain.enums.NotificationType
import kdbrian.github.io.drag.domain.model.Document
import kdbrian.github.io.drag.domain.model.DocumentCategory
import kdbrian.github.io.drag.domain.model.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val subscriptionService: TopicSubscriptionService,
    private val broadcaster: NotificationBroadcaster,
) {

    fun notifySubscribers(document: Document, type: NotificationType, message: String) {
        document.categories.forEach { category -> notifySubscribersOfTopic(document, category, type, message) }
    }

    fun notifySubscribersOfTopic(document: Document, category: DocumentCategory, type: NotificationType, message: String) {
        subscriptionService.subscribersOf(category.categoryId).forEach { subscription ->
            val notification = notificationRepository.save(
                Notification(
                    user = subscription.user,
                    documentId = document.id,
                    documentTitle = document.title,
                    categoryId = category.categoryId,
                    categoryName = category.name,
                    type = type,
                    message = message,
                )
            )
            broadcaster.publish(notification)
        }
    }

    fun mine(userId: String, unreadOnly: Boolean, pageable: Pageable): Page<Notification> =
        if (unreadOnly) notificationRepository.findByUser_IdAndReadFalse(userId, pageable)
        else notificationRepository.findByUser_Id(userId, pageable)

    fun markRead(id: String): Notification {
        val notification = notificationRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Notification[$id] missing.") }
        notification.read = true
        return notificationRepository.save(notification)
    }
}
