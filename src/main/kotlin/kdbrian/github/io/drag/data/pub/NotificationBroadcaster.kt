package kdbrian.github.io.drag.data.pub

import kdbrian.github.io.drag.domain.model.Notification
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class NotificationBroadcaster {
    private val sink = Sinks.many().multicast().onBackpressureBuffer<Notification>()

    fun publish(notification: Notification) {
        sink.tryEmitNext(notification)
    }

    fun stream(): Flux<Notification> = sink.asFlux()
}
