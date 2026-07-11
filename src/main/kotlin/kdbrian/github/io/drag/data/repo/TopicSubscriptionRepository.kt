package kdbrian.github.io.drag.data.repo

import kdbrian.github.io.drag.domain.model.TopicSubscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicSubscriptionRepository : JpaRepository<TopicSubscription, String> {
    fun findByUser_Id(userId: String): List<TopicSubscription>
    fun findByCategory_CategoryId(categoryId: String): List<TopicSubscription>
    fun findByUser_IdAndCategory_CategoryId(userId: String, categoryId: String): TopicSubscription?
    fun deleteByUser_IdAndCategory_CategoryId(userId: String, categoryId: String)
    fun countByCategory_CategoryId(categoryId: String): Long
}
