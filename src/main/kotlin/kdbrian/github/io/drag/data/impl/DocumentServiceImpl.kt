package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.data.repo.DocumentRepository
import kdbrian.github.io.drag.domain.dto.DocumentDto
import kdbrian.github.io.drag.domain.enums.DocumentStatus
import kdbrian.github.io.drag.domain.enums.NotificationType
import kdbrian.github.io.drag.domain.model.Document
import kdbrian.github.io.drag.domain.model.User
import kdbrian.github.io.drag.domain.service.DocumentCategoryService
import kdbrian.github.io.drag.domain.service.DocumentService
import kdbrian.github.io.drag.util.paging.PageParameters
import kdbrian.github.io.drag.util.paging.asPageable
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository,
    private val documentCategoryService: DocumentCategoryService,
    private val notificationService: NotificationService,
) : DocumentService {
    override fun create(documentDto: DocumentDto, uploadedBy: User?): Document {
        return documentRepository.save(
            Document(
                title = documentDto.title,
                authoringSource = documentDto.authoringSource,
                source = documentDto.source,
                summary = documentDto.summary,
                uploadedBy = uploadedBy,
                categories = documentDto.categories.map {
                    documentCategoryService.createCategory(it)
                }.toMutableSet()
            )
        )
    }

    override fun save(document: Document): Document {
        return documentRepository.save(document)
    }

    override fun update(
        id: String,
        documentDto: DocumentDto
    ): Document {
        val orElseThrow = findById(id)
        orElseThrow.title = documentDto.title
        orElseThrow.summary = documentDto.summary
        orElseThrow.aiSummary = documentDto.aiSummary
        orElseThrow.updatedAt = System.currentTimeMillis()

        return documentRepository.save(orElseThrow)
    }

    override fun delete(id: String): Boolean {
        val existsById = documentRepository.existsById(id)
        if (existsById)
            documentRepository.deleteById(id)
        return existsById
    }

    override fun findById(id: String): Document {
        return documentRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException("Document[$id] missing.")
            }
    }

    override fun findAll(pageParameters: PageParameters): Page<Document> {
        return documentRepository.findAll(pageParameters.asPageable)
    }

    override fun findAllProcessed(pageParameters: PageParameters): Page<Document> {
        return documentRepository.findByStatus(DocumentStatus.PROCESSED, pageParameters.asPageable)
    }

    override fun findByTitle(title: String, pageParameters: PageParameters): Page<Document> {
        return documentRepository.findByTitleContainingIgnoreCase(title, pageParameters.asPageable)
    }

    override fun findBySource(source: String, pageParameters: PageParameters): Page<Document> {
        return documentRepository.findBySourceContainingIgnoreCase(source, pageParameters.asPageable)
    }

    override fun findByCategory(categoryId: String, pageParameters: PageParameters): Page<Document> {
        return documentRepository.findByCategories_CategoryId(categoryId, pageParameters.asPageable)
    }

    override fun findByUploader(userId: String, pageParameters: PageParameters): Page<Document> {
        return documentRepository.findByUploadedBy_Id(userId, pageParameters.asPageable)
    }

    override fun search(
        title: String?,
        source: String?,
        pageParameters: PageParameters
    ): Page<Document> {
        return documentRepository.findByTitleContainingIgnoreCaseOrSourceContainingIgnoreCase(
            title ?: "",
            source ?: "",
            pageParameters.asPageable
        )
    }

    override fun existsById(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun saveAll(documents: List<Document>): List<Document> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(ids: List<String>) {
        TODO("Not yet implemented")
    }

    override fun findDocumentsWithoutSummary(pageParameters: PageParameters): Page<Document> {
        return documentRepository.findBySummaryIsEmpty(pageParameters.asPageable)
    }

    override fun findDocumentsWithoutAiSummary(pageParameters: PageParameters): Page<Document> {
        return documentRepository.findByAiSummaryIsEmpty(pageParameters.asPageable)
    }

    override fun findRecentlyUpdated(
        timestamp: Long,
        pageParameters: PageParameters
    ): Page<Document> {
        return documentRepository.findByUpdatedAtGreaterThan(timestamp, pageParameters.asPageable)
    }


    override fun addCategoriesToDocument(vararg id: String, documentId: String): Document {
        val categories = id.map { documentCategoryService.categoryById(it) }
        val document = findById(documentId)
        val existingIds = document.categories.map { it.categoryId }.toSet()
        val newlyAdded = categories.filterNot { it.categoryId in existingIds }
        document.categories.addAll(newlyAdded)
        val saved = documentRepository.save(document)

        newlyAdded.forEach { category ->
            notificationService.notifySubscribersOfTopic(
                saved, category, NotificationType.UPLOADED,
                "Document '${saved.title}' was added to topic '${category.name}'"
            )
        }
        return saved
    }

}