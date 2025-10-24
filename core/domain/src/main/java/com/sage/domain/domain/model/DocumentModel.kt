package com.sage.domain.domain.model

import com.kdbrian.sage.BuildConfig
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DocumentModel(
    val documentId: String = UUID.randomUUID().toString(),
    val documentName: String = "",
    val publicationAt: String = "${System.currentTimeMillis()}",
    val topics: List<String> = emptyList(),
    val publisher: String = "",//not firebase uid but will be loaded into user account and offloaded
    val summary: String = "",
    val isAiSummarizable: Boolean = false,
    var fileInfo: String = "",
    val aiSummary: String = "",
    val uri: String = "",
) {
    companion object {
        const val defaultCollectionName = "${BuildConfig.APPLICATION_ID}/documents_meta/default" //our inventory
        const val generatedCollectionName = "${BuildConfig.APPLICATION_ID}/documents_meta/uploaded"//uploaded by user
    }

}
