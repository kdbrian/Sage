package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.data.repo.DocumentCategoryRepository
import kdbrian.github.io.drag.domain.dto.DocumentCategoryDto
import kdbrian.github.io.drag.domain.model.DocumentCategory
import kdbrian.github.io.drag.domain.service.DocumentCategoryService
import kdbrian.github.io.drag.util.paging.PageParameters
import kdbrian.github.io.drag.util.paging.asPageable
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class DocumentCategoryServiceImpl(
    private val documentCategoryRepository: DocumentCategoryRepository,
) : DocumentCategoryService {
    override fun all(pageParameters: PageParameters): Page<DocumentCategory> {
        return documentCategoryRepository.findAll(pageParameters.asPageable)
    }

    override fun search(
        name: String,
        pageParameters: PageParameters
    ): Page<DocumentCategory> {
        return documentCategoryRepository.findByNameContainingIgnoreCase(name, pageParameters.asPageable)
    }

    override fun createCategory(dto: DocumentCategoryDto): DocumentCategory {
        return documentCategoryRepository.save(
            DocumentCategory(
                name = dto.name,
                description = dto.description,
                source = dto.source
            )
        )
    }

    override fun categoryById(id: String): DocumentCategory {
        return documentCategoryRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException("Document category with ID $id not found")
            }
    }

    override fun updateCategory(
        id: String,
        dto: DocumentCategoryDto
    ): DocumentCategory {

        val category = categoryById(id)
        category.name = dto.name
        category.description = dto.description
        category.updatedAt = System.currentTimeMillis()


        return documentCategoryRepository.save(category)

    }

    override fun delete(id: String): Boolean {
        categoryById(id)
        documentCategoryRepository.deleteById(id)
        return true
    }
}