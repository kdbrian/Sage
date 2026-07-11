package kdbrian.github.io.drag.data.sub

import kdbrian.github.io.drag.data.repo.DocumentRepository
import kdbrian.github.io.drag.domain.enums.DocumentStatus
import kdbrian.github.io.drag.domain.events.DocumentFailedEvent
import kdbrian.github.io.drag.domain.events.DocumentProcessedEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component


@Component
class ProcessingResultListener(
    private val repository: DocumentRepository
) {

    @RabbitListener(queues = ["document-processed-queue"])
    fun handleProcessed(event: DocumentProcessedEvent) {
        updateStatus(event.documentId, DocumentStatus.PROCESSED)
    }

    @RabbitListener(queues = ["document-failed-queue"])
    fun handleFailed(event: DocumentFailedEvent) {
        updateStatus(event.documentId, DocumentStatus.FAILED, event.reason)
    }

    private fun updateStatus(documentId: String, status: DocumentStatus, reason: String? = null) {
        repository.findById(documentId).ifPresent { doc ->
            doc.status = status
            doc.processingError = reason
            doc.updatedAt = System.currentTimeMillis()
            repository.save(doc)
        }
    }
}
