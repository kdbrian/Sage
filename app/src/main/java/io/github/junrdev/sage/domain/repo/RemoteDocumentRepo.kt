package io.github.junrdev.sage.domain.repo

import io.github.junrdev.sage.domain.model.FirebaseUploadedDocument

interface RemoteDocumentRepo {

    suspend fun uploadDocument(onResult: (Result<String>) -> Unit): Result<String>

    suspend fun downloadDocument(onResult: (Result<FirebaseUploadedDocument>) -> Unit)

    suspend fun updateDocument(onResult: (Result<FirebaseUploadedDocument>) -> Unit)

    suspend fun summarizeDocumentWithAi(onResult: (Result<String>) -> Unit)

    suspend fun generateDocumentTopicWithAi(onResult: (Result<String>) -> Unit)

    suspend fun searchDocumentWithName(name: String, onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit)

    suspend fun searchDocumentWithTopic(name: String, onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit)

    suspend fun searchDocumentWithDescriptionOrAiSummary(
        name: String,
        onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit
    )

}