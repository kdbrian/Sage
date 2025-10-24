package com.kdbrian.sage.ui.state

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import com.kdbrian.sage.domain.model.DocumentModel

data class CreateScreenUiState(
    val isUploading: Boolean = false,
    val docUri: Uri? = null,
    val documentName: TextFieldState = TextFieldState(),
    val documentSummary: TextFieldState = TextFieldState(),
    val topics: Set<String> = mutableSetOf()
)

fun CreateScreenUiState.copyDocumentModel() =
    DocumentModel(
        documentName = documentName.text.trim().toString(),
        summary = documentSummary.text.trim().toString(),
        topics = topics.toList(),
        uri = docUri.toString()
    )