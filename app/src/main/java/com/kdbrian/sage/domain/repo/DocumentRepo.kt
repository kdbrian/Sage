package com.kdbrian.sage.domain.repo

import android.net.Uri
import com.kdbrian.sage.domain.model.DocumentModel

interface DocumentRepo {


    suspend fun saveDocumentMetaData(uri: Uri, documentModel: DocumentModel): Result<Boolean>

    suspend fun loadDefaultDocumentsAll(): Result<List<DocumentModel?>>

    suspend fun loadUploadedDocumentsAll(): Result<List<DocumentModel?>>

    suspend fun loadDefaultDocumentsByTopic(topicId: String): Result<List<DocumentModel?>>

    suspend fun loadUploadedDocumentsByTopic(topicId: String): Result<List<DocumentModel?>>

    suspend fun loadDefaultDocumentsByQuery(query: String): Result<List<DocumentModel?>>

    suspend fun loadUploadedDocumentsByQuery(query: String): Result<List<DocumentModel?>>


}