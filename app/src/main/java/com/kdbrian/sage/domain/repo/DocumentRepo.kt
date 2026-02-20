package com.kdbrian.sage.domain.repo

import android.net.Uri
import androidx.paging.PagingData
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.util.Resource
import kotlinx.coroutines.flow.Flow

typealias UploadState = Resource<DocumentModel>


interface DocumentRepo {

    suspend fun simplerSearch(query: String): Result<List<DocumentModel?>>

    fun saveDocumentMetaData(uri: Uri): Flow<UploadState>

    fun loadDefaultDocumentsAll(): Flow<PagingData<DocumentModel>>

    fun loadUploadedDocumentsAll(): Flow<PagingData<DocumentModel>>

    fun loadDefaultDocumentsByTopic(topicId: String): Flow<PagingData<DocumentModel>>

    fun loadUploadedDocumentsByTopic(topicId: String): Flow<PagingData<DocumentModel>>

    fun loadDefaultDocumentsByQuery(query: String): Flow<PagingData<DocumentModel>>

    fun loadUploadedDocumentsByQuery(query: String): Flow<PagingData<DocumentModel>>
}