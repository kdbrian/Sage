package kdbrian.github.io.drag.data.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Service

data class ChunkMatch(
    val documentId: String,
    val chunkIndex: Int,
    val chunkContent: String,
    val distance: Double,
)

/**
 * Reads pdf_document_chunks directly via a native query rather than a JPA
 * entity: that table is created and owned by the Python processor (see
 * VectorStore in processor/pipeline.py), and mapping it as a managed entity
 * here would let Hibernate's ddl-auto try to alter a schema it doesn't own.
 */
@Service
class VectorSearchService(
    @PersistenceContext private val entityManager: EntityManager,
) {
    @Suppress("UNCHECKED_CAST")
    fun search(queryEmbedding: List<Float>, limit: Int): List<ChunkMatch> {
        val vectorLiteral = queryEmbedding.joinToString(prefix = "[", postfix = "]") { it.toString() }

        val rows = entityManager.createNativeQuery(
            """
            SELECT document_id, chunk_index, chunk_content, embedding <=> CAST(:vector AS vector) AS distance
            FROM pdf_document_chunks
            ORDER BY distance ASC
            LIMIT :limit
            """.trimIndent()
        )
            .setParameter("vector", vectorLiteral)
            .setParameter("limit", limit)
            .resultList as List<Array<Any>>

        return rows.map { row ->
            ChunkMatch(
                documentId = row[0] as String,
                chunkIndex = (row[1] as Number).toInt(),
                chunkContent = row[2] as String,
                distance = (row[3] as Number).toDouble(),
            )
        }
    }
}
