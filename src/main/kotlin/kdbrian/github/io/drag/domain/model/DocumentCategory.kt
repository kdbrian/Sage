package kdbrian.github.io.drag.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import java.util.*

@Entity
@Table(
    name = "document_categories",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name"])
    ]
)
class DocumentCategory(
    @Id
    var categoryId: String = UUID.randomUUID().toString().split("-").first(),

    // Owning side (Document.categories) serializes the relation; ignore it here
    // to avoid Document <-> DocumentCategory <-> Document... infinite JSON recursion.
    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    var documents: MutableSet<Document> = mutableSetOf(),
    @Column(nullable = false, unique = true)
    var name: String = "",
    @field:Size(
        min = 130,
        message = "Description must be at least 130 characters long"
    )
    @Column(nullable = false, length = 2000)
    var description: String = "",

    @field:URL(message = "Source must be a valid HTTPS URL")
    @field:Pattern(
        regexp = "^https://.*$",
        message = "Source must use HTTPS"
    )
    @Column(nullable = true)
    var source: String? = null,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
)

