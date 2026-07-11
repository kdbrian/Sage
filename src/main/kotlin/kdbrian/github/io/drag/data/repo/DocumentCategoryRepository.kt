package kdbrian.github.io.drag.data.repo

import kdbrian.github.io.drag.domain.model.DocumentCategory
import kdbrian.github.io.drag.util.paging.defaultPageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentCategoryRepository : JpaRepository<DocumentCategory, String> {
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable = defaultPageable): Page<DocumentCategory>
}
