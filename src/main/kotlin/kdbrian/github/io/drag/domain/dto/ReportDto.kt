package kdbrian.github.io.drag.domain.dto

data class ReportSummary(
    val totalDocuments: Long = 0,
    val byStatus: Map<String, Long> = emptyMap(),
    val totalUsers: Long = 0,
    val totalTopics: Long = 0,
)

data class TopicReport(
    val topicId: String = "",
    val name: String = "",
    val documentCount: Long = 0,
    val subscriberCount: Long = 0,
)

data class UploaderReport(
    val userId: String = "",
    val username: String = "",
    val documentCount: Long = 0,
)
