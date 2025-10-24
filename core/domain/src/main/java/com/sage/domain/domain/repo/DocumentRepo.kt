package com.sage.domain.domain.repo

import android.net.Uri
import com.kdbrian.sage.BuildConfig
import com.sage.domain.domain.model.DocumentModel

interface DocumentRepo {

    val collectionName : String
        get() = BuildConfig.APPLICATION_ID.plus("docs")

    suspend fun simplerSearch(query: String): Result<List<DocumentModel?>>

    suspend fun saveDocumentMetaData(uri: Uri, documentModel: DocumentModel): Result<Boolean>

    suspend fun loadDefaultDocumentsAll(): Result<List<DocumentModel?>>

    suspend fun loadUploadedDocumentsAll(): Result<List<DocumentModel?>>

    suspend fun loadDefaultDocumentsByTopic(topicId: String): Result<List<DocumentModel?>>

    suspend fun loadUploadedDocumentsByTopic(topicId: String): Result<List<DocumentModel?>>

    suspend fun loadDefaultDocumentsByQuery(query: String): Result<List<DocumentModel?>>

    suspend fun loadUploadedDocumentsByQuery(query: String): Result<List<DocumentModel?>>


}