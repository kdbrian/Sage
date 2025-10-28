package com.kdbrian.sage.ui.state

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sage.domain.domain.repo.TopicRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val topicRepo: TopicRepo
) : ViewModel() {

    private val _mutableState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState())
    val uiState = _mutableState.asStateFlow()

    fun updateQuery(query: String) {
        _mutableState.value.searchQuery.setTextAndPlaceCursorAtEnd(query)
    }

    init {
        loadDefaultTopics()
    }

    fun loadDefaultTopics() {
        viewModelScope.launch {

            _mutableState.value = _mutableState.value.copy(isLoading = true)

//            val allTopics = topicRepo.loadDefaultTopics()


        }
    }

}