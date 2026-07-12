package kdbrian.github.io.drag.security

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

private val UPLOAD_PATHS = setOf("/api/documents/upload", "/api/chat/upload")
private val QUERY_PATHS = setOf("/api/chat/query", "/api/chat/query/stream")

private enum class BucketKind { UPLOAD, QUERY, GENERAL }

/**
 * Per-user token buckets, held in memory (fine for a single instance; would
 * need a shared store like Redis behind a load balancer). Uploads get their
 * own, much tighter bucket since they're the expensive operation that kicks
 * off PDF processing. Chat queries get their own bucket too -- wider than
 * uploads (they're read-only), but still capped separately from general
 * traffic since each one costs an embedding call plus a vector scan.
 */
@Component
class RateLimitFilter : OncePerRequestFilter() {

    private val buckets = ConcurrentHashMap<String, Bucket>()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val user = currentUser
        if (user != null) {
            val kind = when (request.requestURI) {
                in UPLOAD_PATHS -> BucketKind.UPLOAD
                in QUERY_PATHS -> BucketKind.QUERY
                else -> BucketKind.GENERAL
            }
            val bucket = buckets.computeIfAbsent("${user.id}:$kind") { newBucket(kind) }
            if (!bucket.tryConsume(1)) {
                response.status = 429
                response.contentType = "application/json"
                response.writer.write("""{"error":"Too many requests, slow down."}""")
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun newBucket(kind: BucketKind): Bucket {
        val bandwidth = when (kind) {
            BucketKind.UPLOAD -> Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)))
            BucketKind.QUERY -> Bandwidth.classic(30, Refill.intervally(30, Duration.ofMinutes(1)))
            BucketKind.GENERAL -> Bandwidth.classic(120, Refill.intervally(120, Duration.ofMinutes(1)))
        }
        return Bucket.builder().addLimit(bandwidth).build()
    }
}
