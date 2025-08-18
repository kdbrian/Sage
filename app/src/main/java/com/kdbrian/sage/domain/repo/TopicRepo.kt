package com.kdbrian.sage.domain.repo

import com.kdbrian.sage.domain.model.TopicItem

interface TopicRepo {

    suspend fun loadTopicInfoById(topicId: String): Result<TopicItem?>
    suspend fun loadDefaultTopics(): Result<List<TopicItem?>>
    suspend fun loadTopicsByQuery(query: String): Result<List<TopicItem?>>
}