package com.kdbrian.sage.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.domain.model.TopicItem
import com.kdbrian.sage.domain.repo.DocumentRepo
import com.kdbrian.sage.domain.repo.TopicRepo
import com.kdbrian.sage.util.AppDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

data class TopicDetailsUiState(
    val isLoading: Boolean = false,
    val topicDetails: TopicItem? = null,
    val message: String? = null,
    val topicDocuments: List<DocumentModel?> = emptyList(),
    val favouriteTopics: MutableSet<String> = mutableSetOf()
)


class TopicDetailsViewModel(
    private val topicRepo: TopicRepo,
    private val appDataStore: AppDataStore,
    private val documentRepo: DocumentRepo
) : ViewModel() {

    private val _mutableState = MutableStateFlow(TopicDetailsUiState())
    val uiState = _mutableState.asStateFlow()

    init {
        loadFavouriteTopics()
    }


    fun loadFavouriteTopics() {
        viewModelScope.launch {
            appDataStore.favouriteTopics.collect {
                _mutableState.value = _mutableState.value.copy(favouriteTopics = it)
            }
        }
    }

    fun loadTopicDetails(topicId: String) {

        viewModelScope.launch {
            _mutableState.value = _mutableState.value.copy(isLoading = true)
            val topicInfoById = topicRepo.loadTopicInfoById(topicId)

            topicInfoById.onSuccess {
                _mutableState.value = _mutableState.value.copy(topicDetails = it)

                launch {
                    _mutableState.value = _mutableState.value.copy(isLoading = true)
                    Timber.d("invoked. ${it?.topicName}")

                    val defaultDocumentsByTopic = documentRepo.loadDefaultDocumentsByTopic(topicId)

                    defaultDocumentsByTopic.onSuccess {
                        _mutableState.value =
                            _mutableState.value.copy(topicDocuments = it, isLoading = false)
                    }

                    defaultDocumentsByTopic.onFailure {
                        _mutableState.value =
                            _mutableState.value.copy(isLoading = false, message = it.message)
                    }
                }
            }


            topicInfoById.onFailure {
                _mutableState.value = _mutableState.value.copy(isLoading = false)
            }

        }
    }


    fun onFavourite() {
        viewModelScope.launch {
            Timber.d("before : ${appDataStore.favouriteTopics.first().size}")
            _mutableState.value.topicDetails?.topicId?.let {
                appDataStore.favouriteTopics.first().add(it)
            }

            Timber.d("updated : ${appDataStore.favouriteTopics.first().size}")
        }
    }

}