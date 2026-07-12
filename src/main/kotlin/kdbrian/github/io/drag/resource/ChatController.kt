package kdbrian.github.io.drag.resource

import jakarta.validation.Valid
import kdbrian.github.io.drag.domain.dto.ChatQueryRequest
import kdbrian.github.io.drag.domain.dto.ChatQueryResponse
import kdbrian.github.io.drag.domain.dto.RetrievedChunk
import kdbrian.github.io.drag.domain.model.Document
import kdbrian.github.io.drag.data.impl.ChatService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

/**
 * Retrieval-only RAG chat surface: upload a PDF, or ask a question against
 * everything uploaded so far. Query responses are the matched chunks plus
 * their source documents' summaries, ranked by vector distance -- no LLM
 * synthesis step, see ChatService.
 */
@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
    private val documentController: DocumentController,
) {

    @PostMapping("/query")
    fun query(@Valid @RequestBody request: ChatQueryRequest): ChatQueryResponse =
        chatService.query(request.query)

    /** Same retrieval as /query, delivered as it's assembled rather than in one
     *  response -- each matched chunk is its own SSE event, followed by a
     *  terminal "done" event once the result set is exhausted. */
    @PostMapping("/query/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun queryStream(@Valid @RequestBody request: ChatQueryRequest): SseEmitter {
        val emitter = SseEmitter(30_000L)
        Thread {
            try {
                val response = chatService.query(request.query)
                if (!response.matched) {
                    emitter.send(SseEmitter.event().name("degraded").data(response.message ?: ""))
                } else {
                    response.results.forEach { chunk: RetrievedChunk ->
                        emitter.send(SseEmitter.event().name("chunk").data(chunk))
                    }
                }
                emitter.send(SseEmitter.event().name("done").data(request.query))
                emitter.complete()
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
        }.start()
        return emitter
    }

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestPart multipartFile: MultipartFile,
        @RequestPart title: String,
        @RequestPart summary: String,
        @RequestPart source: String,
    ): Document = documentController.uploadDocument(multipartFile, title, summary, source)
}
