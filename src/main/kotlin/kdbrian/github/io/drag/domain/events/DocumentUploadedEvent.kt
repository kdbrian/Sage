package kdbrian.github.io.drag.domain.events

data class DocumentUploadedEvent(
    val eventId: String = "",
    val documentId: String = "",
    val filename: String? = null,
    val storagePath: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) : Event

