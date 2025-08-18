package com.kdbrian.sage.ui.state

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.sage.domain.repo.TopicRepo
import com.kdbrian.sage.ui.util.UiTimeOut
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

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

            val allTopics = topicRepo.loadDefaultTopics()

            allTopics.onSuccess {
                Timber.d("done ${it.size}")
                delay(UiTimeOut.timeOut)
                _mutableState.value = _mutableState.value.copy(
                    topics = it,
                    isLoading = false,
                    message = "Loaded ${it.size} topics."
                )
            }

            allTopics.onFailure {
                Timber.d("Failed ${it.message}")
                delay(UiTimeOut.timeOut)
                _mutableState.value = _mutableState.value.copy(
                    message = it.message ?: "Something went wrong : ${it.message}",
                    isLoading = false
                )
            }


        }
    }

}