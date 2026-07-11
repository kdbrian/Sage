package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.data.repo.DocumentCategoryRepository
import kdbrian.github.io.drag.data.repo.DocumentRepository
import kdbrian.github.io.drag.data.repo.UserRepository
import kdbrian.github.io.drag.domain.dto.ReportSummary
import kdbrian.github.io.drag.domain.dto.TopicReport
import kdbrian.github.io.drag.domain.dto.UploaderReport
import kdbrian.github.io.drag.domain.enums.DocumentStatus
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val documentRepository: DocumentRepository,
    private val categoryRepository: DocumentCategoryRepository,
    private val userRepository: UserRepository,
    private val subscriptionService: TopicSubscriptionService,
) {

    fun summary() = ReportSummary(
        totalDocuments = documentRepository.count(),
        byStatus = DocumentStatus.entries.associate { it.name to documentRepository.countByStatus(it) },
        totalUsers = userRepository.count(),
        totalTopics = categoryRepository.count(),
    )

    fun byTopic(): List<TopicReport> = categoryRepository.findAll().map {
        TopicReport(
            topicId = it.categoryId,
            name = it.name,
            documentCount = it.documents.size.toLong(),
            subscriberCount = subscriptionService.subscriberCount(it.categoryId),
        )
    }

    fun byUploader(): List<UploaderReport> = userRepository.findAll().map {
        UploaderReport(
            userId = it.id,
            username = it.username,
            documentCount = documentRepository.countByUploadedBy_Id(it.id),
        )
    }
}
