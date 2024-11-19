package io.github.junrdev.sage.domain.model

import java.time.LocalDate

open class AppDocument(
    val dateAdded: String = LocalDate.now().toString()
)

data class FirebaseUploadedDocument(
    val documentId: String,
    val topic: String? = null, //CS, Math, Bio...
    val title: String,
    val description: String,
    val downloadUrl: String,

    //ai fields
    val topicWithAi: Boolean = false,
    val topicAi: String? = null,

    val summarizedWithAi: Boolean = false,
    val aiSummary: String? = null,

    val downloadCount: Long,
    val owner: String,//ref to user
) : AppDocument()