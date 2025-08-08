package com.kdbrian.sage.domain.repo

import com.kdbrian.sage.domain.model.TopicItem

interface TopicRepo {
    suspend fun loadAllTopics(): Result<List<TopicItem>>
    suspend fun loadAllTopicsByQuery(query: String): Result<List<TopicItem>>
}