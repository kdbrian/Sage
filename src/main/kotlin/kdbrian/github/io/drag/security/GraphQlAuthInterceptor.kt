package kdbrian.github.io.drag.security

import org.springframework.graphql.server.WebSocketGraphQlInterceptor
import org.springframework.graphql.server.WebSocketSessionInfo
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GraphQlAuthInterceptor(
    private val jwtService: JwtService,
) : WebSocketGraphQlInterceptor {

    override fun handleConnectionInitialization(
        sessionInfo: WebSocketSessionInfo,
        connectionInitPayload: MutableMap<String, Any>
    ): Mono<Any> {
        (connectionInitPayload["authorization"] as? String)
            ?.removePrefix("Bearer ")
            ?.let(jwtService::parseUserId)
            ?.let { sessionInfo.attributes["userId"] = it }

        return Mono.empty()
    }
}
