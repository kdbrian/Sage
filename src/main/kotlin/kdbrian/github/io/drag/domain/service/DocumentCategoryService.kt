package kdbrian.github.io.drag.domain.service

import kdbrian.github.io.drag.domain.dto.DocumentCategoryDto
import kdbrian.github.io.drag.domain.model.DocumentCategory
import kdbrian.github.io.drag.util.paging.PageParameters
import org.springframework.data.domain.Page


interface DocumentCategoryService {
    fun all(pageParameters: PageParameters = PageParameters()): Page<DocumentCategory>
    fun search(name: String, pageParameters: PageParameters = PageParameters()): Page<DocumentCategory>
    fun createCategory(dto: DocumentCategoryDto): DocumentCategory
    fun categoryById(id: String): DocumentCategory
    fun updateCategory(id: String, dto: DocumentCategoryDto): DocumentCategory
    fun delete(id: String): Boolean
}