package io.github.junrdev.sage.domain.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import io.github.junrdev.sage.domain.model.FirebaseUploadedDocument
import io.github.junrdev.sage.domain.model.FirebaseUploadDocumentDto


interface FirebaseStorageDocumentsRepo {

    val DOCUMENTS_FOLDER: StorageReference
    val DOCUMENTS_REFERENCE: CollectionReference

    suspend fun uploadDocument(dto: FirebaseUploadDocumentDto):  Result<String>

    suspend fun downloadDocument(firebaseUploadedDocument: FirebaseUploadedDocument) : Result<FirebaseUploadedDocument>

//    suspend fun updateDocument(onResult: (Result<FirebaseUploadedDocument>) -> Unit)
    suspend fun summarizeDocumentWithAi(onResult: (Result<String>) -> Unit)

    suspend fun generateDocumentTopicWithAi(onResult: (Result<String>) -> Unit)

    suspend fun searchDocumentWithName(name: String, onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit)

    suspend fun searchDocumentWithTopic(name: String, onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit)

    suspend fun searchDocumentWithDescriptionOrAiSummary(
        name: String,
        onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit
    )

}