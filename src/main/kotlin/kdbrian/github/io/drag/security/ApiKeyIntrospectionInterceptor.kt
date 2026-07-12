package kdbrian.github.io.drag.security

import graphql.ErrorType
import graphql.ExecutionResultImpl
import graphql.GraphqlErrorBuilder
import graphql.language.Field
import graphql.language.OperationDefinition
import graphql.parser.Parser
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.graphql.support.DefaultExecutionGraphQlResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/** Requests authenticated only via the API key may browse the schema, never run real queries/mutations. */
@Component
class ApiKeyIntrospectionInterceptor : WebGraphQlInterceptor {

    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        if (principal == API_KEY_PRINCIPAL && !isIntrospectionOnly(request.document)) {
            // Returned as a normal GraphQL error (HTTP 200 + errors[]) rather than thrown:
            // spring-graphql's WebMvc handler resolves the response via an async
            // CompletableFuture, so an exception here surfaces through the servlet
            // container's generic /error dispatch instead of GlobalErrorHandler,
            // losing this message. Building the error response directly sidesteps that.
            val error = GraphqlErrorBuilder.newError()
                .message("The API key only allows schema introspection; sign in for data access.")
                .errorType(ErrorType.ExecutionAborted)
                .build()
            val result = ExecutionResultImpl(error)
            return Mono.just(WebGraphQlResponse(DefaultExecutionGraphQlResponse(request.toExecutionInput(), result)))
        }
        return chain.next(request)
    }

    private fun isIntrospectionOnly(query: String): Boolean {
        val document = runCatching { Parser().parseDocument(query) }.getOrNull() ?: return false
        val operation = document.definitions.filterIsInstance<OperationDefinition>().firstOrNull() ?: return false
        return operation.selectionSet.selections.all { (it as? Field)?.name?.startsWith("__") == true }
    }
}
