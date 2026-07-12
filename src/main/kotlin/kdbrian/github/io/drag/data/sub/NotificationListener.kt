package kdbrian.github.io.drag.data.sub

import kdbrian.github.io.drag.data.impl.NotificationService
import kdbrian.github.io.drag.data.repo.DocumentRepository
import kdbrian.github.io.drag.domain.enums.NotificationType
import kdbrian.github.io.drag.domain.events.DocumentFailedEvent
import kdbrian.github.io.drag.domain.events.DocumentProcessedEvent
import kdbrian.github.io.drag.domain.events.DocumentUploadedEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** Fans document lifecycle events out to everyone subscribed to the document's topics. */
@Component
class NotificationListener(
    private val documentRepository: DocumentRepository,
    private val notificationService: NotificationService,
) {

    // @Transactional has to sit on these RabbitListener-invoked methods, not on
    // the shared `notify` helper: Spring's transactional proxy only intercepts
    // calls that come in from OUTSIDE the bean (like the listener container
    // calling these), not self-invocations of `notify(...)` on `this`. Putting
    // it on `notify` alone silently does nothing, and the fetched Document's
    // lazy `categories` blow up with LazyInitializationException once the
    // (nonexistent) transaction closes.

    @RabbitListener(queues = ["notify-uploaded-queue"])
    @Transactional
    fun handleUploaded(event: DocumentUploadedEvent) =
        notify(event.documentId, NotificationType.UPLOADED, "Document '${event.filename}' was added")

    @RabbitListener(queues = ["notify-processed-queue"])
    @Transactional
    fun handleProcessed(event: DocumentProcessedEvent) =
        notify(event.documentId, NotificationType.PROCESSED, "Document '${event.filename}' finished processing (${event.chunkCount} chunks)")

    @RabbitListener(queues = ["notify-failed-queue"])
    @Transactional
    fun handleFailed(event: DocumentFailedEvent) =
        notify(event.documentId, NotificationType.FAILED, "Document processing failed: ${event.reason}")

    private fun notify(documentId: String, type: NotificationType, message: String) {
        documentRepository.findById(documentId).ifPresent { document ->
            notificationService.notifySubscribers(document, type, message)
        }
    }
}
