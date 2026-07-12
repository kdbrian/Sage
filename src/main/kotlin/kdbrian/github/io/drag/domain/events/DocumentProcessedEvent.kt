package kdbrian.github.io.drag.domain.events

data class DocumentProcessedEvent(
    val eventId: String = "",
    val documentId: String = "",
    val filename: String? = null,
    val chunkCount: Int = 0,
    val vectorCount: Int = 0,
    val storagePath: String? = null,
    val summary: String? = null,
    val topics: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
) : Event