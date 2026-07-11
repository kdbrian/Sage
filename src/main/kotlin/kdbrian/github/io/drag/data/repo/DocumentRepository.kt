package kdbrian.github.io.drag.data.repo

import kdbrian.github.io.drag.domain.enums.DocumentStatus
import kdbrian.github.io.drag.domain.model.Document
import kdbrian.github.io.drag.util.paging.defaultPageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : JpaRepository<Document, String>,
    JpaSpecificationExecutor<Document> {

    fun findByStatus(status: DocumentStatus, pageable: Pageable = defaultPageable): Page<Document>


    fun findByTitleContainingIgnoreCase(
        title: String,
        pageable: Pageable = defaultPageable
    ): Page<Document>

    fun findBySourceContainingIgnoreCase(
        source: String,
        pageable: Pageable = defaultPageable
    ): Page<Document>

    fun findByCategories_CategoryId(
        categoryId: String,
        pageable: Pageable = defaultPageable
    ): Page<Document>

    fun findBySummaryIsEmpty(pageable: Pageable = defaultPageable): Page<Document>

    fun findByAiSummaryIsEmpty(pageable: Pageable = defaultPageable): Page<Document>

    fun findByUpdatedAtGreaterThan(
        timestamp: Long,
        pageable: Pageable = defaultPageable
    ): Page<Document>

    fun existsByTitle(title: String): Boolean


    fun findByTitleContainingIgnoreCaseOrSourceContainingIgnoreCase(
        title: String,
        source: String,
        pageable: Pageable = defaultPageable
    ): Page<Document>


    fun existsBySource(source: String): Boolean

    fun findByAuthoringSource(key: String): List<Document>

    fun findByUploadedBy_Id(userId: String, pageable: Pageable = defaultPageable): Page<Document>

    fun countByStatus(status: DocumentStatus): Long

    fun countByUploadedBy_Id(userId: String): Long

}