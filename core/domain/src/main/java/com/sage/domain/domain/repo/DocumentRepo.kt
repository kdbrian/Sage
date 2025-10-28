package com.sage.domain.domain.repo

import android.net.Uri
import com.sage.domain.domain.model.DocumentModel

interface DocumentRepo {

    val collectionName : String
        get() = "com.sage/docs/defaults"

    suspend fun simplerSearch(query: String): Result<List<DocumentModel?>>

    suspend fun saveDocumentMetaData(uri: Uri, documentModel: DocumentModel): Result<Boolean>

    suspend fun loadDocumentsAll(): Result<List<DocumentModel?>>

    suspend fun loadDocumentsById(documentId: String): Result<DocumentModel?>
    suspend fun loadDocumentsByTopic(topicId: String): Result<List<DocumentModel?>>

    suspend fun loadDocumentsByQuery(query: String): Result<List<DocumentModel?>>


}