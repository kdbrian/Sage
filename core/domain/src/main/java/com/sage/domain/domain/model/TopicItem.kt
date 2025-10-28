package com.sage.domain.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TopicItem(
    val topicId: String = "",
    val topicName: String = "",
    val dateAdded: Long = System.currentTimeMillis(),
){
    companion object{
        const val defaultCollectionName = "com.sage/topics/default"
        const val generatedCollectionName = "com.sage/topics/gen_ai"
    }
}
