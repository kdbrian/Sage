package com.kdbrian.sage.ui.state

import androidx.compose.foundation.text.input.TextFieldState

data class HomeScreenUiState(
    val searchQuery: TextFieldState = TextFieldState(),
    val topics: List<String> = emptyList()
)