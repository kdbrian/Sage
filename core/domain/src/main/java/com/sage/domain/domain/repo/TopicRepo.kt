package com.sage.domain.domain.repo

import com.sage.domain.domain.model.TopicItem

interface TopicRepo {

    suspend fun loadTopicInfoById(topicId: String): Result<TopicItem?>
    suspend fun loadDefaultTopics(): Result<List<TopicItem?>>
    suspend fun loadTopicsByQuery(query: String): Result<List<TopicItem?>>
}