package com.kdbrian.sage.ui.screens

import coil3.compose.AsyncImagePainter
import com.kdbrian.sage.domain.model.DocumentModel

data class DocumentDetailsUiState(
    val documentModel: DocumentModel? = null,
    val isLoading: Boolean = false
)