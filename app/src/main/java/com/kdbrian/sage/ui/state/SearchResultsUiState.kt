package com.kdbrian.sage.ui.state

import com.kdbrian.sage.domain.model.DocumentModel

data class SearchResultsUiState(
    val isLoading: Boolean = false,
    val documentsFromDefault: List<DocumentModel?> = emptyList(),
    val documentsFromUploaded: List<DocumentModel?> = emptyList(),
)