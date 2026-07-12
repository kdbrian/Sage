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

private const val UPLOAD_PATH = "/api/documents/upload"

/**
 * Per-user token buckets, held in memory (fine for a single instance; would
 * need a shared store like Redis behind a load balancer). Uploads get their
 * own, much tighter bucket since they're the expensive operation that kicks
 * off PDF processing.
 */
@Component
class RateLimitFilter : OncePerRequestFilter() {

    private val buckets = ConcurrentHashMap<String, Bucket>()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val user = currentUser
        if (user != null) {
            val isUpload = request.requestURI == UPLOAD_PATH
            val bucket = buckets.computeIfAbsent("${user.id}:${if (isUpload) "upload" else "general"}") { newBucket(isUpload) }
            if (!bucket.tryConsume(1)) {
                response.status = 429
                response.contentType = "application/json"
                response.writer.write("""{"error":"Too many requests, slow down."}""")
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun newBucket(upload: Boolean): Bucket {
        val bandwidth = if (upload)
            Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)))
        else
            Bandwidth.classic(120, Refill.intervally(120, Duration.ofMinutes(1)))
        return Bucket.builder().addLimit(bandwidth).build()
    }
}
