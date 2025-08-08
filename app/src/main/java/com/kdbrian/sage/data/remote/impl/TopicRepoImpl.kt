package com.kdbrian.sage.data.remote.impl

import com.kdbrian.sage.domain.model.TopicItem
import com.kdbrian.sage.domain.repo.TopicRepo

class TopicRepoImpl(
//    private val firest
): TopicRepo {
    override suspend fun loadAllTopics(): Result<List<TopicItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadAllTopicsByQuery(query: String): Result<List<TopicItem>> {
        TODO("Not yet implemented")
    }
}