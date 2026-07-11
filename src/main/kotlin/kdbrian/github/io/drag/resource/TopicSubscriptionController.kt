package kdbrian.github.io.drag.resource

import kdbrian.github.io.drag.data.impl.TopicSubscriptionService
import kdbrian.github.io.drag.domain.model.TopicSubscription
import kdbrian.github.io.drag.security.requireCurrentUser
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topics")
class TopicSubscriptionController(
    private val subscriptionService: TopicSubscriptionService,
) {

    @PostMapping("/{topicId}/subscribe")
    fun subscribe(@PathVariable topicId: String): TopicSubscription =
        subscriptionService.subscribe(requireCurrentUser().id, topicId)

    @DeleteMapping("/{topicId}/subscribe")
    fun unsubscribe(@PathVariable topicId: String) =
        subscriptionService.unsubscribe(requireCurrentUser().id, topicId)

    @GetMapping("/subscriptions")
    fun mine(): List<TopicSubscription> = subscriptionService.mine(requireCurrentUser().id)
}
