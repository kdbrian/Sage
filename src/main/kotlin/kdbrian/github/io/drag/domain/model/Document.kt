@file:OptIn(ExperimentalUuidApi::class)

package kdbrian.github.io.drag.domain.model

import jakarta.persistence.*
import kdbrian.github.io.drag.domain.enums.DocumentStatus
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
@Table(
    name = "documents",
)
class Document(
    @Id
    var id: String = Uuid.random().toString().split("-").first(),
    var title: String = "",
    @ManyToMany
    @JoinTable(
        name = "document_category_links",
        joinColumns = [JoinColumn(name = "document_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")],
    )
    var categories: MutableSet<DocumentCategory> = mutableSetOf(),
    var summary: String = "",
    var source: String = "",
    var authoringSource: String = "",
    var status : DocumentStatus? = null,
    var aiSummary: String = "",
    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    var uploadedBy: User? = null,
    var processingError: String? = null,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
)


