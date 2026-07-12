package kdbrian.github.io.drag.util

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import jakarta.validation.ConstraintViolationException
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component

/** GraphQL counterpart to GlobalErrorHandler -- same exception types, mapped to
 *  clean GraphQL errors instead of REST responses. Unrecognized exceptions fall
 *  through to Spring GraphQL's default (generic INTERNAL_ERROR) handling. */
@Component
class GraphQlExceptionResolver : DataFetcherExceptionResolverAdapter() {

    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        val (message, type) = when (ex) {
            is ConstraintViolationException ->
                ex.constraintViolations.joinToString("; ") { "${it.propertyPath}: ${it.message}" } to ErrorType.BAD_REQUEST
            is IllegalArgumentException -> (ex.message ?: "Invalid request") to ErrorType.BAD_REQUEST
            is IllegalStateException -> (ex.message ?: "Not authorized") to ErrorType.UNAUTHORIZED
            else -> return null
        }
        return GraphqlErrorBuilder.newError(env).message(message).errorType(type).build()
    }
}
