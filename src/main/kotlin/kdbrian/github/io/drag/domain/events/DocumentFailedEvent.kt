package kdbrian.github.io.drag.domain.events

data class DocumentFailedEvent(
    val eventId: String = "",
    val documentId: String = "",
    val reason: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) : Event