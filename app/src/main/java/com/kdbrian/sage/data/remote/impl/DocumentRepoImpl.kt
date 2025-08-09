package com.kdbrian.sage.data.remote.impl

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.domain.repo.DocumentRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID

class DocumentRepoImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : DocumentRepo {


    override suspend fun saveDocumentMetaData(
        uri: Uri,
        documentModel: DocumentModel
    ): Result<Boolean> =
        withContext(Dispatchers.Default) {
            Timber.d("uri: $uri")
            Timber.d("documentModel: $documentModel")

            try {
                // Step 1: Create a new document reference
                val documentReference =
                    firebaseFirestore.collection(DocumentModel.generatedCollectionName).document()

                // Step 2: Create a storage reference using the same document ID
                val storageRef = firebaseStorage.reference
                    .child("${DocumentModel.generatedCollectionName}/files/${documentReference.id}")

                // Step 3: Upload file to Firebase Storage
                storageRef.putFile(uri).await()

                // Step 4: Get the file's download URL
                val downloadUrl = storageRef.downloadUrl.await().toString()


                // Step 5: Save metadata in Firestore (including storage URL)
                documentReference
                    .set(
                        documentModel.copy(
                            publisher = UUID.randomUUID().toString().split("-").first(),
                            documentId = documentReference.id, uri = downloadUrl
                        )
                    )
                    .await()

                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun loadDefaultDocumentsAll(): Result<List<DocumentModel?>> = withContext(
        Dispatchers.Default
    ) {
        try {
            val documentSnapshots =
                firebaseFirestore.collection(DocumentModel.defaultCollectionName)
                    .get()
                    .await()

            val documentModels = documentSnapshots.documents
                .map { it.toObject(DocumentModel::class.java) }
                .toList()
            Result.success(documentModels)
        } catch (e: Exception) {
            Result.failure(e)

        }

    }

    override suspend fun loadUploadedDocumentsAll(): Result<List<DocumentModel?>> = withContext(
        Dispatchers.Default
    ) {
        try {
            val documentSnapshots =
                firebaseFirestore.collection(DocumentModel.generatedCollectionName)
                    .get()
                    .await()

            val documentModels = documentSnapshots.documents
                .map { it.toObject(DocumentModel::class.java) }
                .toList()

            Result.success(documentModels)
        } catch (e: Exception) {
            Result.failure(e)

        }

    }

    override suspend fun loadDefaultDocumentsByTopic(topicId: String): Result<List<DocumentModel?>> =
        withContext(Dispatchers.Default) {
            try {
                val documentSnapshots =
                    firebaseFirestore.collection(DocumentModel.defaultCollectionName)
                        .whereArrayContains("topics", topicId)
                        .get()
                        .await()
                val documentModels = documentSnapshots.documents
                    .map { it.toObject(DocumentModel::class.java) }
                    .toList()

                Result.success(documentModels)
            } catch (e: Exception) {
                Result.failure(e)

            }
        }

    override suspend fun loadUploadedDocumentsByTopic(topicId: String): Result<List<DocumentModel?>> =
        withContext(Dispatchers.Default) {

            try {
                val documentSnapshots =
                    firebaseFirestore.collection(DocumentModel.generatedCollectionName)
                        .whereArrayContains("topics", topicId)
                        .get()
                        .await()
                val documentModels = documentSnapshots.documents
                    .map { it.toObject(DocumentModel::class.java) }
                    .toList()

                Result.success(documentModels)
            } catch (e: Exception) {
                Result.failure(e)

            }
        }

    override suspend fun loadDefaultDocumentsByQuery(query: String): Result<List<DocumentModel?>> =
        withContext(Dispatchers.Default) {
            loadDefaultDocumentsAll()
                .fold(
                    onSuccess = { documents ->
                        val documentModels = documents.filter {
                            (it?.documentName?.contains(query, ignoreCase = true) == true) ||
                                    (it?.summary?.contains(query, ignoreCase = true) == true)
                        }

                        Result.success(documentModels)
                    },
                    onFailure = {
                        Result.failure(it)
                    }

                )
        }

    override suspend fun loadUploadedDocumentsByQuery(query: String): Result<List<DocumentModel?>> =
        withContext(Dispatchers.Default) {
            loadUploadedDocumentsAll()
                .fold(
                    onSuccess = { documents ->
                        val documentModels = documents.filter {
                            (it?.documentName?.contains(query, ignoreCase = true) == true) ||
                                    (it?.summary?.contains(query, ignoreCase = true) == true)
                        }
                        Result.success(documentModels)
                    },
                    onFailure = {
                        Result.failure(it)
                    }
                )
        }
}