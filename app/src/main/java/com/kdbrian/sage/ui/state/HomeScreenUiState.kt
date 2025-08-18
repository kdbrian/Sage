package com.kdbrian.sage.ui.state

import androidx.compose.foundation.text.input.TextFieldState
import com.kdbrian.sage.domain.model.TopicItem

data class HomeScreenUiState(
    val searchQuery: TextFieldState = TextFieldState(),
    val topics: List<TopicItem?> = emptyList(),
    val isLoading: Boolean = false,
    val message: String = ""
)