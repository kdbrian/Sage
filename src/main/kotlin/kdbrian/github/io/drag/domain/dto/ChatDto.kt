package kdbrian.github.io.drag.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChatQueryRequest(
    @field:NotBlank(message = "Query must not be blank")
    @field:Size(max = 500, message = "Query must be at most 500 characters")
    val query: String = "",
)

data class RetrievedChunk(
    val documentId: String,
    val documentTitle: String,
    val chunkIndex: Int,
    val chunkContent: String,
    val distance: Double,
    val documentSummary: String?,
    val aiSummary: String?,
)

data class ChatQueryResponse(
    val query: String,
    /** false when nothing in the vector store cleared the relevance threshold --
     *  the query was out of context for the current document set. */
    val matched: Boolean,
    val message: String?,
    val results: List<RetrievedChunk>,
)
