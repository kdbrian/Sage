package com.kdbrian.sage.data.remote.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.kdbrian.sage.domain.model.CategoryModel
import com.kdbrian.sage.domain.repo.CategoryRepo
import com.kdbrian.sage.util.dispatchIo
import kotlinx.coroutines.tasks.await

class FirebaseCategoryRepoImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : CategoryRepo {
    override suspend fun loadCategoryInfoById(topicId: String): Result<CategoryModel?> =
        dispatchIo {
            val queryDocumentSnapshots = firestore.collection(collectionName)
                .document(topicId)
                .get()
                .await()

            val categoryModel = queryDocumentSnapshots.toObject(CategoryModel::class.java)
            Result.success(categoryModel)
        }

    override suspend fun loadDefaultTopics(): Result<List<CategoryModel?>> = dispatchIo {
        val queryDocumentSnapshots = firestore.collection(collectionName).get().await()
        Result.success(queryDocumentSnapshots.toObjects(CategoryModel::class.java))
    }

    override suspend fun loadTopicsByQuery(query: String): Result<List<CategoryModel?>> =
        dispatchIo {
            Result.success(
                firestore.collection(collectionName).whereEqualTo("label", query).get().await()
                    .toObjects(CategoryModel::class.java)
            )
        }
}