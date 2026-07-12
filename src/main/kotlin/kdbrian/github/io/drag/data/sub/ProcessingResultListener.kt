package kdbrian.github.io.drag.data.sub

import kdbrian.github.io.drag.data.impl.NotificationService
import kdbrian.github.io.drag.data.repo.DocumentRepository
import kdbrian.github.io.drag.domain.enums.DocumentStatus
import kdbrian.github.io.drag.domain.enums.NotificationType
import kdbrian.github.io.drag.domain.events.DocumentFailedEvent
import kdbrian.github.io.drag.domain.events.DocumentProcessedEvent
import kdbrian.github.io.drag.domain.service.DocumentCategoryService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class ProcessingResultListener(
    private val repository: DocumentRepository,
    private val documentCategoryService: DocumentCategoryService,
    private val notificationService: NotificationService,
) {

    // @Transactional has to sit directly on this @RabbitListener-invoked method
    // (see NotificationListener for the same note): the document's categories
    // are lazily loaded, and open-in-view doesn't help here since this thread
    // was never an HTTP request thread. Without one transaction spanning the
    // read, the lazy access below, and the write, categories access throws.
    @RabbitListener(queues = ["document-processed-queue"])
    @Transactional
    fun handleProcessed(event: DocumentProcessedEvent) {
        val document = repository.findById(event.documentId).orElse(null) ?: return
        document.status = DocumentStatus.PROCESSED
        document.updatedAt = System.currentTimeMillis()
        if (!event.summary.isNullOrBlank()) {
            document.aiSummary = event.summary
        }

        val existingNames = document.categories.map { it.name.lowercase() }.toSet()
        val newCategories = event.topics
            .filter { it.lowercase() !in existingNames }
            .map { documentCategoryService.findOrCreateByName(it) }
        document.categories.addAll(newCategories)

        val saved = repository.save(document)

        newCategories.forEach { category ->
            notificationService.notifySubscribersOfTopic(
                saved, category, NotificationType.PROCESSED,
                "Document '${saved.title}' was categorized under topic '${category.name}'"
            )
        }
    }

    @RabbitListener(queues = ["document-failed-queue"])
    @Transactional
    fun handleFailed(event: DocumentFailedEvent) {
        repository.findById(event.documentId).ifPresent { doc ->
            doc.status = DocumentStatus.FAILED
            doc.processingError = event.reason
            doc.updatedAt = System.currentTimeMillis()
            repository.save(doc)
        }
    }
}
