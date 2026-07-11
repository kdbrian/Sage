package kdbrian.github.io.drag.data.pub

import kdbrian.github.io.drag.config.DOCUMENT_EXCHANGE
import kdbrian.github.io.drag.domain.events.Event
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

interface EventPublisher {

    fun publishEvent(
        exchange: String = DOCUMENT_EXCHANGE,
        routingKey: String = "document.uploaded",
        event: Event
    )

}

@Service
class EventPublisherImpl(
    private val rabbitTemplate: RabbitTemplate
): EventPublisher {

    override fun publishEvent(
        exchange: String,
        routingKey: String,
        event: Event
    ) {
        rabbitTemplate.convertAndSend(
            exchange,
            routingKey,
            event
        )
    }
}