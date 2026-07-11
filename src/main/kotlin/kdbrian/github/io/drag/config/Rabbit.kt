package kdbrian.github.io.drag.config

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Declarables
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


const val DOCUMENT_EXCHANGE: String = "documents.exchange"

/**
 * queue name -> routing key it binds to on [DOCUMENT_EXCHANGE].
 * One queue per (consumer, event type) pair keeps every listener single-type,
 * so no payload-type dispatch is needed on the AMQP side.
 */
private val TOPOLOGY = mapOf(
    "pdf-processing-queue" to "document.uploaded",     // consumed by the Python processor
    "document-processed-queue" to "document.processed", // Spring: mark document PROCESSED
    "document-failed-queue" to "document.failed",       // Spring: mark document FAILED
    "notify-uploaded-queue" to "document.uploaded",      // Spring: fan out to topic subscribers
    "notify-processed-queue" to "document.processed",
    "notify-failed-queue" to "document.failed",
)

@Configuration
class Rabbit {

    @Bean
    fun documentExchange(): TopicExchange = TopicExchange(DOCUMENT_EXCHANGE, true, false)

    @Bean
    fun documentTopology(exchange: TopicExchange): Declarables = Declarables(
        TOPOLOGY.flatMap { (queueName, routingKey) ->
            val queue = QueueBuilder.durable(queueName).build()
            listOf(queue, BindingBuilder.bind(queue).to(exchange).with(routingKey))
        }
    )

    @Bean
    fun converter(): JacksonJsonMessageConverter = JacksonJsonMessageConverter()

    @Bean
    fun rabbitTemplate(
        connectionFactory: org.springframework.amqp.rabbit.connection.ConnectionFactory
    ): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = converter()
        return template
    }

}
