package io.github.junrdev.sage.domain.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import io.github.junrdev.sage.domain.model.FirebaseUploadDocumentDto
import io.github.junrdev.sage.domain.model.FirebaseUploadedDocument


interface FirebaseStorageDocumentsRepo {

    val DOCUMENTS_FOLDER: StorageReference
    val DOCUMENTS_REFERENCE: CollectionReference

    suspend fun uploadDocument(dto: FirebaseUploadDocumentDto):  Result<String>

    suspend fun downloadDocument(firebaseUploadedDocument: FirebaseUploadedDocument) : Result<FirebaseUploadedDocument>

    /*
    * use either file path from local storage or firebase link
    * */
    suspend fun summarizeDocumentWithAi(path : String): Result<String>


    /*
    * use either file path from local storage or firebase link
    * */
    suspend fun generateDocumentTopicWithAi(path : String): Result<String>

    suspend fun searchDocumentWithName(name: String): Result<List<FirebaseUploadedDocument>>

    suspend fun searchDocumentWithTopic(name: String): Result<List<FirebaseUploadedDocument>>

    suspend fun searchDocumentWithDescriptionOrAiSummary(name: String): Result<List<FirebaseUploadedDocument>>

    //    suspend fun updateDocument(onResult: (Result<FirebaseUploadedDocument>) -> Unit)
}
