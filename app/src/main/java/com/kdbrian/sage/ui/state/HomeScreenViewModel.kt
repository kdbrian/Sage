package com.kdbrian.sage.ui.state

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel : ViewModel() {


    private val _mutableState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState())
    private val uiState = _mutableState.asStateFlow()

    fun updateQuery(query: String) {
        _mutableState.value.searchQuery.setTextAndPlaceCursorAtEnd(query)
    }


}