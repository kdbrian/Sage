package com.kdbrian.sage.domain.model

import com.google.firebase.firestore.DocumentSnapshot
import com.kdbrian.sage.BuildConfig
import kotlinx.serialization.Serializable

@Serializable
data class DocumentModel(
    val id: String="",
    val title: String="",
    val author: String="",
    val coverUrl: String="",
    val category: String="",
    val rating: Float = 0f,
    val pageCount: Int = 0,
    val chapterCount: Int = 0,
    val publicationAt: String? = null,
    val topics: List<String> = emptyList(),
    val publisher: String = "",//not firebase uid but will be loaded into user account and offloaded
    val summary: String = "",
    val isAiSummarizable: String = "",
    var fileInfo: String = "",
    val aiSummary: String = "",
    val description: String = "",
    val uri: String = "",
) {
    companion object {
        val defaultCollectionName =
            "${BuildConfig.APPLICATION_ID}/documents_meta/default" //our inventory
        val generatedCollectionName =
            "${BuildConfig.APPLICATION_ID}/documents_meta/uploaded"//uploaded by user


        val sampleBooks = listOf(
            DocumentModel(
                id = "1",
                title = "Six Crimson Cranes",
                author = "Elizabeth Lim",
                coverUrl = "https://covers.openlibrary.org/b/id/12547191-L.jpg",
                category = "fantasy",
                rating = 4.3f,
                pageCount = 464,
                description = "A princess is exiled from her kingdom, cursed to be mute, and must save her six brothers."
            ),
            DocumentModel(
                id = "2",
                title = "The Name of the Wind",
                author = "Patrick Rothfuss",
                coverUrl = "https://covers.openlibrary.org/b/id/8235125-L.jpg",
                category = "fantasy",
                rating = 4.5f,
                pageCount = 662
            ),
            DocumentModel(
                id = "3",
                title = "A Court of Thorns and Roses",
                author = "Sarah J. Maas",
                coverUrl = "https://covers.openlibrary.org/b/id/10521270-L.jpg",
                category = "fantasy",
                rating = 4.1f,
                pageCount = 419
            )
        )
        val sampleCategories = listOf(
            CategoryModel("fantasy", "Fantasy"),
            CategoryModel("artdesign", "Art Design"),
            CategoryModel("psychology", "Psychology"),
            CategoryModel("music", "Music"),
            CategoryModel("romance", "Romance")
        )
    }

}




// DocumentModelExt.kt — Firestore mapping helpers

fun DocumentModel.toMap(): Map<String, Any?> = mapOf(
    "id"               to id,
    "title"            to title,
    "author"           to author,
    "coverUrl"         to coverUrl,
    "category"         to category,
    "rating"           to rating,
    "pageCount"        to pageCount,
    "publicationAt"    to publicationAt,
    "topics"           to topics,
    "publisher"        to publisher,
    "summary"          to summary,           // defaults ""
    "isAiSummarizable" to isAiSummarizable,
    "fileInfo"         to fileInfo,
    "aiSummary"        to aiSummary,         // defaults ""
    "description"      to description,
    "uri"              to uri,
)

fun DocumentSnapshot.toDocumentModel(): DocumentModel? = runCatching {
    DocumentModel(
        id               = getString("id") ?: id,
        title            = getString("title") ?: "",
        author           = getString("author") ?: "",
        coverUrl         = getString("coverUrl") ?: "",
        category         = getString("category") ?: "",
        rating           = (get("rating") as? Number)?.toFloat() ?: 0f,
        pageCount        = (get("pageCount") as? Number)?.toInt() ?: 0,
        publicationAt    = getString("publicationAt"),
        topics           = (get("topics") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
        publisher        = getString("publisher") ?: "",
        summary          = getString("summary") ?: "",
        isAiSummarizable = getString("isAiSummarizable") ?: "",
        fileInfo         = getString("fileInfo") ?: "",
        aiSummary        = getString("aiSummary") ?: "",
        description      = getString("description") ?: "",
        uri              = getString("uri") ?: "",
    )
}.getOrNull()



