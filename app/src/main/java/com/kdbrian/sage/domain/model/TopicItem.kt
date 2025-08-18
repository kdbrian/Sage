package com.kdbrian.sage.domain.model

import com.kdbrian.sage.BuildConfig
import kotlinx.serialization.Serializable

@Serializable
data class TopicItem(
    val topicId: String = "",
    val topicName: String = "",
    val dateAdded: Long = System.currentTimeMillis(),
){
    companion object{
        const val defaultCollectionName = "${BuildConfig.APPLICATION_ID}/topics/default"
        const val generatedCollectionName = "${BuildConfig.APPLICATION_ID}/topics/gen_ai"
    }
}
