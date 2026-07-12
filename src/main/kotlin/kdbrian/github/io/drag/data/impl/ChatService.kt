package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.domain.dto.ChatQueryResponse
import kdbrian.github.io.drag.domain.dto.RetrievedChunk
import kdbrian.github.io.drag.domain.service.DocumentService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

private const val DEGRADED_MESSAGE =
    "I couldn't find anything in the current document store relevant to that question. " +
        "Try rephrasing, or upload a document that covers it."

/** Retrieval-only RAG: no generated answer, just the most relevant chunks and
 *  their documents' summaries, ranked by vector distance. */
@Service
class ChatService(
    private val embeddingClient: EmbeddingClient,
    private val vectorSearchService: VectorSearchService,
    private val documentService: DocumentService,
    @Value("\${chat.similarity-threshold:0.6}") private val similarityThreshold: Double,
    @Value("\${chat.result-limit:5}") private val resultLimit: Int,
) {

    fun query(query: String): ChatQueryResponse {
        val embedding = embeddingClient.embed(query)
        val matches = vectorSearchService.search(embedding, resultLimit)
            .filter { it.distance <= similarityThreshold }

        if (matches.isEmpty()) {
            return ChatQueryResponse(query = query, matched = false, message = DEGRADED_MESSAGE, results = emptyList())
        }

        val results = matches.map { match ->
            val document = runCatching { documentService.findById(match.documentId) }.getOrNull()
            RetrievedChunk(
                documentId = match.documentId,
                documentTitle = document?.title ?: "Unknown document",
                chunkIndex = match.chunkIndex,
                chunkContent = match.chunkContent,
                distance = match.distance,
                documentSummary = document?.summary,
                aiSummary = document?.aiSummary,
            )
        }

        return ChatQueryResponse(query = query, matched = true, message = null, results = results)
    }
}
