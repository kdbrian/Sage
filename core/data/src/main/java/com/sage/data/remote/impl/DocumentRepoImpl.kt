package com.sage.data.remote.impl

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sage.domain.domain.model.DocumentModel
import com.sage.domain.domain.repo.DocumentRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID

class DocumentRepoImpl : DocumentRepo {

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()

    private val collectionReference = firebaseFirestore
        .collection(collectionName)

    override suspend fun simplerSearch(
        query: String
    ): Result<List<DocumentModel?>> =
        withContext(Dispatchers.IO) {
            try {

                val collectionReference = firebaseFirestore.collection(collectionName)

                val documentSnapshots = collectionReference
                    .get()
                    .await()

                val documentModels = documentSnapshots.documents.map {
                    it.toObject(DocumentModel::class.java)
                }.toList()

                Result.success(documentModels)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun saveDocumentMetaData(
        uri: Uri,
        documentModel: DocumentModel
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            Timber.d("uri: $uri")
            Timber.d("documentModel: $documentModel")

            try {
                // Step 1: Create a new document reference
                val documentReference =
                    firebaseFirestore.collection(collectionName).document()

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

    override suspend fun loadDocumentsAll(): Result<List<DocumentModel?>> =
        withContext(Dispatchers.IO) {
            try {
                val documentSnapshots = collectionReference.get().await()
                if (documentSnapshots.isEmpty)
                    Result.success(emptyList())
                else {
                    val documentModels = documentSnapshots.toObjects(DocumentModel::class.java)
                    Result.success(documentModels)
                }


            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun loadDocumentsById(documentId: String): Result<DocumentModel?> = withContext(Dispatchers.IO){
        try {
            val documentSnapshots = collectionReference.document(documentId).get().await()
            if (documentSnapshots.exists())
                Result.success(null)
            else {
                val documentModels = documentSnapshots.toObject(DocumentModel::class.java)
                Result.success(documentModels)
            }


        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadDocumentsByTopic(topicId: String): Result<List<DocumentModel?>> =
        withContext(
            Dispatchers.IO
        ) {
            try {

                //send topic to backend

                //backend cunks + knn -> LLM -> Firestore -> Result

                //await results
                Result.success(emptyList())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun loadDocumentsByQuery(query: String): Result<List<DocumentModel?>> =
        withContext(Dispatchers.IO) {
            try {

                Result.success(emptyList())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }


}