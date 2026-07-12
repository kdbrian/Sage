package kdbrian.github.io.drag.data.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.requiredBody

private data class EmbedRequest(val text: String)
private data class EmbedResponse(val embedding: List<Float>, val dimension: Int)

/** Calls the processor's internal-only /embed endpoint so a chat query is embedded
 *  with the exact same model used on stored chunks (never routed through the
 *  gateway -- reachable on the Docker network only). */
@Service
class EmbeddingClient(
    @Value("\${processor.url}") processorUrl: String,
) {
    private val client = RestClient.builder().baseUrl(processorUrl).build()

    fun embed(text: String): List<Float> {
        val response = client.post()
            .uri("/embed")
            .body(EmbedRequest(text))
            .retrieve()
            .requiredBody<EmbedResponse>()
        return response.embedding
    }
}
