package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.data.repo.TopicSubscriptionRepository
import kdbrian.github.io.drag.data.repo.UserRepository
import kdbrian.github.io.drag.domain.model.TopicSubscription
import kdbrian.github.io.drag.domain.service.DocumentCategoryService
import org.springframework.stereotype.Service

@Service
class TopicSubscriptionService(
    private val subscriptionRepository: TopicSubscriptionRepository,
    private val userRepository: UserRepository,
    private val categoryService: DocumentCategoryService,
) {

    fun subscribe(userId: String, topicId: String): TopicSubscription {
        subscriptionRepository.findByUser_IdAndCategory_CategoryId(userId, topicId)?.let { return it }

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User[$userId] missing.") }
        val category = categoryService.categoryById(topicId)

        return subscriptionRepository.save(TopicSubscription(user = user, category = category))
    }

    fun unsubscribe(userId: String, topicId: String) {
        subscriptionRepository.deleteByUser_IdAndCategory_CategoryId(userId, topicId)
    }

    fun mine(userId: String): List<TopicSubscription> = subscriptionRepository.findByUser_Id(userId)

    fun subscribersOf(topicId: String): List<TopicSubscription> =
        subscriptionRepository.findByCategory_CategoryId(topicId)

    fun subscriberCount(topicId: String): Long = subscriptionRepository.countByCategory_CategoryId(topicId)
}
