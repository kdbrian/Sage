package com.kdbrian.sage.data.paging

// FirestorePagingSource.kt
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.domain.model.toDocumentModel
import kotlinx.coroutines.tasks.await

class FirestorePagingSource(
    private val query: Query,
    private val pageSize: Long = 20,
) : PagingSource<DocumentSnapshot, DocumentModel>() {

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, DocumentModel>): DocumentSnapshot? = null

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, DocumentModel> =
        try {
            val pageQuery = params.key
                ?.let { query.startAfter(it).limit(pageSize) }
                ?: query.limit(pageSize)

            val snapshot = pageQuery.get().await()
            val documents = snapshot.documents.mapNotNull { it.toDocumentModel() }
            val nextKey = snapshot.documents.lastOrNull()?.takeIf { snapshot.size() >= pageSize }

            LoadResult.Page(
                data = documents,
                prevKey = null, // forward-only paging
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}