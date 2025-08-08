package com.kdbrian.sage.data.remote.impl

import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.kdbrian.sage.domain.model.TopicItem
import com.kdbrian.sage.domain.repo.TopicRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TopicRepoImpl(
    private val firestore: FirebaseFirestore
) : TopicRepo {


    override suspend fun loadAllTopics(): Result<List<TopicItem?>> =
        withContext(Dispatchers.Default) {
            try {
                val documentSnapshots = firestore.collection(TopicItem.collectionName).get().await()
                if (!documentSnapshots.isEmpty) {
                    val topicItems = documentSnapshots.documents
                        .map { it.toObject(TopicItem::class.java) }
                        .toList()

                    Result.success(
                        topicItems
                    )
                } else {
                    Result.failure(Exception("No topics found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun loadAllTopicsByQuery(query: String): Result<List<TopicItem?>> =
        withContext(Dispatchers.Default) {
            try {
                val documentSnapshots = firestore
                    .collection(TopicItem.collectionName)
                    .whereEqualTo("topicName", query)
                    .get()
                    .await()

                if (!documentSnapshots.isEmpty) {
                    val topicItems = documentSnapshots.documents
                        .map { it.toObject(TopicItem::class.java) }
                        .toList()

                    Result.success(
                        topicItems
                    )
                } else {
                    Result.failure(Exception("No topics found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

}