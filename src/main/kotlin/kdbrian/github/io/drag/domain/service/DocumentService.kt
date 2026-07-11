package kdbrian.github.io.drag.domain.service

import kdbrian.github.io.drag.domain.dto.DocumentDto
import kdbrian.github.io.drag.domain.model.Document
import kdbrian.github.io.drag.domain.model.User
import kdbrian.github.io.drag.util.paging.PageParameters
import org.springframework.data.domain.Page

interface DocumentService {

    // CRUD
    fun create(documentDto: DocumentDto, uploadedBy: User? = null): Document
    fun save(document: Document): Document

    fun update(id: String, documentDto: DocumentDto): Document

    fun delete(id: String): Boolean

    fun findById(id: String): Document

    // Pagination & Sorting
    fun findAll(pageParameters: PageParameters = PageParameters()): Page<Document>
    fun findAllProcessed(pageParameters: PageParameters = PageParameters()): Page<Document>

    // Search
    fun findByTitle(title: String, pageParameters: PageParameters = PageParameters()): Page<Document>

    fun findBySource(source: String, pageParameters: PageParameters = PageParameters()): Page<Document>

    fun findByCategory(categoryId: String, pageParameters: PageParameters = PageParameters()): Page<Document>

    fun findByUploader(userId: String, pageParameters: PageParameters = PageParameters()): Page<Document>

    fun search(
        title: String?,
        source: String?,
        pageParameters: PageParameters = PageParameters()
    ): Page<Document>

    fun existsById(id: String): Boolean

    fun count(): Long

    fun saveAll(documents: List<Document>): List<Document>

    fun deleteAll(ids: List<String>)

    fun findDocumentsWithoutSummary(pageParameters: PageParameters = PageParameters()): Page<Document>

    fun findDocumentsWithoutAiSummary(pageParameters: PageParameters = PageParameters()): Page<Document>

    fun findRecentlyUpdated(timestamp: Long, pageParameters: PageParameters = PageParameters()): Page<Document>

    fun addCategoriesToDocument(vararg id: String, documentId: String): Document


}
