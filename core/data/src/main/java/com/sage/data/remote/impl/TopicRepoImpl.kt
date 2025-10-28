package com.sage.data.remote.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.sage.domain.domain.model.TopicItem
import com.sage.domain.domain.repo.TopicRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber

class TopicRepoImpl : TopicRepo {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    override suspend fun loadTopicInfoById(topicId: String): Result<TopicItem?> =
        withContext(Dispatchers.Default) {
            try {
                val documentSnapshots =
                    firestore.collection(TopicItem.defaultCollectionName).document(topicId).get()
                        .await()
                val topicDocument = documentSnapshots.toObject(TopicItem::class.java)
                if (topicDocument != null) {

                    Timber.tag("topicDocument").d(Json.encodeToString(topicDocument))

                    Result.success(topicDocument)
                } else {
                    Result.failure(Exception("No topics found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun loadDefaultTopics(): Result<List<TopicItem?>> =
        withContext(Dispatchers.Default) {
            try {
                val documentSnapshots =
                    firestore.collection(TopicItem.defaultCollectionName).get().await()
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

    override suspend fun loadTopicsByQuery(query: String): Result<List<TopicItem?>> =
        withContext(Dispatchers.Default) {
            try {
                val documentSnapshots = firestore
                    .collection(TopicItem.defaultCollectionName)
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