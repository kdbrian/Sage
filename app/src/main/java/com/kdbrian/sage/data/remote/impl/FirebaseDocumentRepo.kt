package com.kdbrian.sage.data.remote.impl

// FirebaseDocumentRepo.kt
import android.net.Uri
import androidx.paging.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.kdbrian.sage.data.paging.FirestorePagingSource
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.domain.model.InAppActivity
import com.kdbrian.sage.domain.model.toDocumentModel
import com.kdbrian.sage.domain.model.toMap
import com.kdbrian.sage.domain.repo.AppActivityService
import com.kdbrian.sage.domain.repo.DocumentRepo
import com.kdbrian.sage.domain.repo.UploadState
import com.kdbrian.sage.util.Resource
import com.kdbrian.sage.util.dispatchIo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

private const val PAGE_SIZE = 20
private const val COLLECTION_DEFAULT = "documents_default"
private const val COLLECTION_UPLOADED = "documents_uploaded"

class FirebaseDocumentRepo(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val appActivityService: AppActivityService
) : DocumentRepo {


    // ─── Upload ───────────────────────────────────────────────────────────────

    override fun saveDocumentMetaData(uri: Uri): Flow<UploadState> = callbackFlow {
        trySend(Resource.Loading(0f))

        val userId = auth.currentUser?.uid
            ?: run { trySend(Resource.Error("User not authenticated")); close(); return@callbackFlow }

        val fileName = uri.lastPathSegment ?: "document_${System.currentTimeMillis()}"
        val storageRef = storage.reference.child("uploads/$userId/$fileName")

        val uploadTask = storageRef.putFile(uri)

        uploadTask.addOnProgressListener { snapshot ->
            val progress = snapshot.bytesTransferred.toFloat() / snapshot.totalByteCount
            trySend(Resource.Loading(progress))
        }

        try {
            uploadTask.await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            val documentId = firestore.collection(COLLECTION_UPLOADED).document().id
            val document = DocumentModel(
                id = documentId,
                title = fileName,
                uri = downloadUrl,
                fileInfo = uri.toString(),
                // summary, aiSummary default to ""
            )

            firestore.collection(COLLECTION_UPLOADED)
                .document(documentId)
                .set(document.toMap())
                .await()

            dispatchIo {
                appActivityService.logActivity(
                    InAppActivity
                        .Builder()
                        .name("Save Document ${document.title.uppercase()}_${document.id}")
                        .build()
                )
            }

            trySend(Resource.Loading(1f))
            trySend(Resource.Success(document))
        } catch (e: Exception) {
            trySend(Resource.Error(e.message ?: "Upload failed"))
        }

        close()
        awaitClose()
    }

    // ─── Paged Reads ──────────────────────────────────────────────────────────

    override fun loadDefaultDocumentsAll(): Flow<PagingData<DocumentModel>> =
        pagedFlow { firestore.collection(COLLECTION_DEFAULT) }

    override fun loadUploadedDocumentsAll(): Flow<PagingData<DocumentModel>> =
        pagedFlow { firestore.collection(COLLECTION_UPLOADED) }

    override fun loadDefaultDocumentsByTopic(topicId: String): Flow<PagingData<DocumentModel>> =
        pagedFlow {
            firestore.collection(COLLECTION_DEFAULT)
                .whereArrayContains("topics", topicId)
        }

    override fun loadUploadedDocumentsByTopic(topicId: String): Flow<PagingData<DocumentModel>> =
        pagedFlow {
            firestore.collection(COLLECTION_UPLOADED)
                .whereArrayContains("topics", topicId)
        }

    override fun loadDefaultDocumentsByQuery(query: String): Flow<PagingData<DocumentModel>> =
        pagedFlow {
            firestore.collection(COLLECTION_DEFAULT)
                .orderBy("title")
                .startAt(query)
                .endAt("$query\uf8ff")
        }

    override fun loadUploadedDocumentsByQuery(query: String): Flow<PagingData<DocumentModel>> =
        pagedFlow {
            firestore.collection(COLLECTION_UPLOADED)
                .orderBy("title")
                .startAt(query)
                .endAt("$query\uf8ff")
        }

    override suspend fun toggleFavourite(documentModel: DocumentModel) =
        dispatchIo {
            val documentReference = firestore
                .collection(COLLECTION_DEFAULT)
                .document("${auth.currentUser?.uid}/favourites/${documentModel.id}")

            val documentSnapshot = documentReference.get().await()
            val exists = documentSnapshot.exists()
            if (!exists) {
                documentReference
                    .set(documentModel.toMap())
                    .await()
            } else {
                documentReference.delete()
                    .await()
            }

            dispatchIo {
                appActivityService.logActivity(
                    InAppActivity
                        .Builder()
                        .name("Toggle Favourite Document ${documentModel.title.uppercase()}_${documentModel.id}")
                        .build()
                )
            }

            Result.success(exists.not())
        }

    // ─── Simple Search ────────────────────────────────────────────────────────

    override suspend fun simplerSearch(query: String): Result<List<DocumentModel?>> =
        runCatching {
            val lower = query.lowercase()
            val snapshot =
                firestore.collection(COLLECTION_DEFAULT)
                    .orderBy("title")
                    .startAt(lower)
                    .endAt("$lower\uf8ff")
                    .limit(50)
                    .get()
                    .await()
            snapshot.documents.map { it.toDocumentModel() }
        }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun pagedFlow(queryBuilder: () -> Query): Flow<PagingData<DocumentModel>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 5,
            ),
            pagingSourceFactory = { FirestorePagingSource(queryBuilder()) }
        ).flow
}