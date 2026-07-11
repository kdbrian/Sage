package kdbrian.github.io.drag.data.sub

import kdbrian.github.io.drag.data.impl.NotificationService
import kdbrian.github.io.drag.data.repo.DocumentRepository
import kdbrian.github.io.drag.domain.enums.NotificationType
import kdbrian.github.io.drag.domain.events.DocumentFailedEvent
import kdbrian.github.io.drag.domain.events.DocumentProcessedEvent
import kdbrian.github.io.drag.domain.events.DocumentUploadedEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

/** Fans document lifecycle events out to everyone subscribed to the document's topics. */
@Component
class NotificationListener(
    private val documentRepository: DocumentRepository,
    private val notificationService: NotificationService,
) {

    @RabbitListener(queues = ["notify-uploaded-queue"])
    fun handleUploaded(event: DocumentUploadedEvent) =
        notify(event.documentId, NotificationType.UPLOADED, "Document '${event.filename}' was added")

    @RabbitListener(queues = ["notify-processed-queue"])
    fun handleProcessed(event: DocumentProcessedEvent) =
        notify(event.documentId, NotificationType.PROCESSED, "Document '${event.filename}' finished processing (${event.chunkCount} chunks)")

    @RabbitListener(queues = ["notify-failed-queue"])
    fun handleFailed(event: DocumentFailedEvent) =
        notify(event.documentId, NotificationType.FAILED, "Document processing failed: ${event.reason}")

    private fun notify(documentId: String, type: NotificationType, message: String) {
        documentRepository.findById(documentId).ifPresent { document ->
            notificationService.notifySubscribers(document, type, message)
        }
    }
}
