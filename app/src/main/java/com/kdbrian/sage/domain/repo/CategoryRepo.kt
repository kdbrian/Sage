package com.kdbrian.sage.domain.repo

import com.kdbrian.sage.BuildConfig
import com.kdbrian.sage.domain.model.CategoryModel


interface CategoryRepo {

    val collectionName : String
        get() = "${BuildConfig.APPLICATION_ID}/categories"

    suspend fun loadCategoryInfoById(topicId: String): Result<CategoryModel?>
    suspend fun loadDefaultTopics(): Result<List<CategoryModel?>>
    suspend fun loadTopicsByQuery(query: String): Result<List<CategoryModel?>>
}