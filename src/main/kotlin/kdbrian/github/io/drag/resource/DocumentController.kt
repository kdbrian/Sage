@file:OptIn(ExperimentalUuidApi::class)

package kdbrian.github.io.drag.resource

import kdbrian.github.io.drag.data.pub.EventPublisher
import kdbrian.github.io.drag.domain.dto.DocumentDto
import kdbrian.github.io.drag.domain.events.DocumentUploadedEvent
import kdbrian.github.io.drag.domain.model.Document
import kdbrian.github.io.drag.domain.service.DocumentService
import kdbrian.github.io.drag.domain.service.FileStorageService
import kdbrian.github.io.drag.security.requireCurrentUser
import kdbrian.github.io.drag.util.paging.PageParameters
import org.springframework.data.domain.Page
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@RestController
@RequestMapping("/api/documents")
@Controller
class DocumentController(
    private val documentService: DocumentService,
    private val publisher: EventPublisher,
    private val fileStorageService: FileStorageService
) {

    @GetMapping("/")
    @QueryMapping
    fun allDocs(
        @RequestBody @Argument parameters: PageParameters,
        @RequestParam(required = false, defaultValue = "false") @Argument mine: Boolean
    ): Page<Document> {
        val page = if (mine) documentService.findByUploader(requireCurrentUser().id, parameters)
        else documentService.findAll(parameters)

        return page.map {
            it.source = "${ServletUriComponentsBuilder.fromCurrentContextPath().toUriString()}/${it.source}"
            it
        }
    }


    @GetMapping("/{id}")
    @QueryMapping
    fun findById(
        @PathVariable @Argument id: String
    ): Document {
        return documentService.findById(id)
    }

    @GetMapping("/title")
    @QueryMapping
    fun findByTitle(
        @RequestParam @Argument title: String,
        @RequestBody @Argument parameters: PageParameters
    ): Page<Document> {
        return documentService.findByTitle(title, parameters)
    }

    @GetMapping("/source")
    @QueryMapping
    fun findBySource(
        @RequestParam @Argument source: String,
        @RequestBody @Argument parameters: PageParameters
    ): Page<Document> {
        return documentService.findBySource(source, parameters)
    }


    @GetMapping("/category/{categoryId}/")
    @QueryMapping
    fun findByCategory(
        @PathVariable @Argument categoryId: String,
        @RequestBody @Argument parameters: PageParameters
    ): Page<Document> {
        return documentService.findByCategory(categoryId, parameters)
    }


    @GetMapping("/range")
    @QueryMapping
    fun findRecentlyUpdated(
        @PathVariable @Argument timestamp: Long,
        @RequestBody @Argument parameters: PageParameters
    ): Page<Document> {
        return documentService.findRecentlyUpdated(timestamp, parameters)
    }

    @PostMapping("/create")
    @MutationMapping
    fun createDocument(
        @Argument @RequestBody dto: DocumentDto
    ): Document {
        return documentService.create(dto, requireCurrentUser())
    }

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @MutationMapping
    fun uploadDocument(
        @RequestPart multipartFile: MultipartFile,
        @RequestPart title: String,
        @RequestPart summary: String,
        @RequestPart source: String
    ): Document {
        val url = fileStorageService.saveFile(multipartFile)
        val document = documentService.create(
            DocumentDto(
                authoringSource = source,
                source = url,
                title = title,
                summary = summary
            ),
            requireCurrentUser()
        )

        publisher.publishEvent(
            event = DocumentUploadedEvent(
                eventId = Uuid.random().toString().split("-").random(),
                documentId = document.id,
                filename = document.title,
                storagePath = document.source,
                timestamp = System.currentTimeMillis()
            ),
        )

        return document
    }

    @PatchMapping("/document/{documentId}/add-categories/")
    @MutationMapping
    fun addCategoriesToDocument(
        @Argument @RequestParam("categories") vararg categories: String,
        @Argument @PathVariable documentId: String,
    ): Document {
        return documentService.addCategoriesToDocument(
            id = categories,
            documentId = documentId
        )
    }

}
