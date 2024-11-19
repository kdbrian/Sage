package io.github.junrdev.sage.domain.model

import android.net.Uri
import java.time.LocalDate

open class AppDocument(
    val dateAdded: String = LocalDate.now().toString()
)

data class FirebaseUploadedDocument(
    val documentId: String="",
    val topic: List<String> = emptyList(), //CS, Math, Bio...
    val title: String="",
    val description: String="",
    val downloadUrl: String="",

    //ai fields
    val topicWithAi: Boolean = false,
    val topicAi: String? = null,

    val summarizedWithAi: Boolean = false,
    val aiSummary: String? = null,

    val downloadCount: Long = 0L,
    val owner: String="",//ref to user
) : AppDocument()

data class FirebaseUploadDocumentDto(
    val topic: List<String> = emptyList(), //CS, Math, Bio...
    val title: String,
    val description: String,
    val uri : Uri
)