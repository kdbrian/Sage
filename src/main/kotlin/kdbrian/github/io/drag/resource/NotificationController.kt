package kdbrian.github.io.drag.resource

import kdbrian.github.io.drag.data.impl.NotificationService
import kdbrian.github.io.drag.data.pub.NotificationBroadcaster
import kdbrian.github.io.drag.domain.model.Notification
import kdbrian.github.io.drag.security.requireCurrentUser
import kdbrian.github.io.drag.util.paging.PageParameters
import kdbrian.github.io.drag.util.paging.asPageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/notifications")
@Controller
class NotificationController(
    private val notificationService: NotificationService,
    private val broadcaster: NotificationBroadcaster,
) {

    @GetMapping
    fun mine(
        @RequestParam(defaultValue = "false") unreadOnly: Boolean,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): Page<Notification> =
        notificationService.mine(
            requireCurrentUser().id,
            unreadOnly,
            PageParameters(page, size, mapOf("createdAt" to Sort.Direction.DESC)).asPageable
        )

    @PatchMapping("/{id}/read")
    fun markRead(@PathVariable id: String): Notification = notificationService.markRead(id)

    @SubscriptionMapping
    fun notifications(@ContextValue(name = "userId", required = false) userId: String?): Flux<Notification> =
        if (userId == null) Flux.empty() else broadcaster.stream().filter { it.user.id == userId }
}
