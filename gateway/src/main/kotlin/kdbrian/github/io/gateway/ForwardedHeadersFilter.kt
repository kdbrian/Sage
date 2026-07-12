package kdbrian.github.io.gateway

import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

/**
 * NettyRoutingFilter builds the proxied request's headers exclusively via
 * registered HttpHeadersFilter beans (not by reusing whatever headers sit on
 * the mutated ServerHttpRequest, and not the built-in XForwardedHeadersFilter
 * in this setup either) — so absolute URLs the backend builds (GraphiQL's
 * /graph redirect, document download links) otherwise leak the internal
 * Docker hostname instead of the address clients actually used.
 */
@Component
class ForwardedHeadersFilter : HttpHeadersFilter {

    override fun filter(input: HttpHeaders, exchange: ServerWebExchange): HttpHeaders {
        val request = exchange.request
        val headers = HttpHeaders()
        headers.addAll(input)
        headers.set("X-Forwarded-Host", request.headers.getFirst("Host") ?: request.uri.host)
        headers.set("X-Forwarded-Proto", request.uri.scheme ?: "http")
        headers.set("X-Forwarded-Port", (request.uri.port.takeIf { it > 0 } ?: 80).toString())
        return headers
    }
}
