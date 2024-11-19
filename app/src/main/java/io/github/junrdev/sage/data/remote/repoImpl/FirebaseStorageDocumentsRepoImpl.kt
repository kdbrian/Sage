package io.github.junrdev.sage.data.remote.repoImpl

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import io.github.junrdev.sage.domain.model.FirebaseUploadDocumentDto
import io.github.junrdev.sage.domain.model.FirebaseUploadedDocument
import io.github.junrdev.sage.domain.repo.FirebaseStorageDocumentsRepo
import kotlinx.coroutines.tasks.await
import java.io.File

class FirebaseStorageDocumentsRepoImpl : FirebaseStorageDocumentsRepo {

    private val DOCUMENTS_UPLOAD_PATH = "Sage-documents-v1"
    private val DOCUMENTS_METADATA_PATH = "$DOCUMENTS_UPLOAD_PATH-metaInf"

    //get storage
    private val STORAGEINSTANCE: FirebaseStorage
        get() = Firebase.storage

    private val FIRESTOREINSTANCE: FirebaseFirestore
        get() = Firebase.firestore

    //reference to documents folder
    override val DOCUMENTS_FOLDER: StorageReference
        get() = STORAGEINSTANCE.getReference(
            DOCUMENTS_UPLOAD_PATH
        )

    override val DOCUMENTS_REFERENCE: CollectionReference
        get() = FIRESTOREINSTANCE.collection(DOCUMENTS_METADATA_PATH)


    private val currentUser = Firebase.auth.currentUser

    override suspend fun uploadDocument(
        dto: FirebaseUploadDocumentDto
    ): Result<String> {
        return try {
            // Reference to the file in Firebase Storage
            val fileRef = DOCUMENTS_FOLDER.child(dto.title)

            // Upload file to Firebase Storage
            fileRef.putFile(dto.uri).await()

            // Fetch the download URL
            val downloadUrl = fileRef.downloadUrl.await().toString()

            // Create a Firestore document reference
            val documentReference = DOCUMENTS_REFERENCE.document()

            // Populate the document metadata
            val documentInfo = FirebaseUploadedDocument(
                title = dto.title,
                description = dto.description,
                documentId = documentReference.id,
                downloadUrl = downloadUrl,
                owner = currentUser?.uid ?: "unknown_user"
            )

            // Save document metadata to Firestore
            documentReference.set(documentInfo).await()

            Result.success("Doc#${documentReference.id} saved successfully.")
        } catch (e: Exception) {
            // Handle any errors and return a failure result
            Result.failure(Exception("Error uploading document: ${e.message}", e))
        }
    }


    override suspend fun downloadDocument(
        firebaseUploadedDocument: FirebaseUploadedDocument,
    ): Result<FirebaseUploadedDocument> {
        return try {
            // Reference to the file in Firebase Storage
            val fileRef = DOCUMENTS_FOLDER.child(firebaseUploadedDocument.title)

            // Specify the local file location for the download (temporary file in cache or a directory)
            val localFile = File.createTempFile(firebaseUploadedDocument.title, null)

            // Start the file download
            fileRef.getFile(localFile).await()

            // Increment the download count in Firestore
            DOCUMENTS_REFERENCE.document(firebaseUploadedDocument.documentId)
                .update("downloadCount", firebaseUploadedDocument.downloadCount + 1)
                .await()

            // Return the updated document with the new download count
            val updatedDocument = firebaseUploadedDocument.copy(
                downloadCount = firebaseUploadedDocument.downloadCount + 1
            )

            Result.success(updatedDocument)
        } catch (e: Exception) {
            // Handle errors and pass them to the result
            Result.failure(e)
        }
    }


    override suspend fun summarizeDocumentWithAi(onResult: (Result<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun generateDocumentTopicWithAi(onResult: (Result<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun searchDocumentWithName(
        name: String,
        onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun searchDocumentWithTopic(
        name: String,
        onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun searchDocumentWithDescriptionOrAiSummary(
        name: String,
        onResult: (Result<List<FirebaseUploadedDocument>>) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}