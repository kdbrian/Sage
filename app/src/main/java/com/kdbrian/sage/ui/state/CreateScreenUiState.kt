package com.kdbrian.sage.ui.state

import android.net.Uri

data class CreateScreenUiState(
    val docUri: Uri? = null,
    val topics: List<String> = emptyList()
)