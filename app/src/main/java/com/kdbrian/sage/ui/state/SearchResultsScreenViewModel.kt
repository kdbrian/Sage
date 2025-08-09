package com.kdbrian.sage.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.sage.domain.repo.DocumentRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class SearchResultsScreenViewModel(
    private val documentRepo: DocumentRepo
) : ViewModel() {

    private val _mutableState = MutableStateFlow(SearchResultsUiState())
    val uiState = _mutableState.asStateFlow()

    private val messageChannel = Channel<String>()
    val messages = messageChannel.receiveAsFlow()
    private val UITimeout = 1200L


    fun loadSearchResults(query: String) {
        viewModelScope.launch {
            _mutableState.value = _mutableState.value.copy(isLoading = true)

            if (query.contains(":")) {
                val row = query.split(":").first()
                val rowValue = query.split(":").last()

                val simplerSearch = documentRepo.simplerSearch(
                    row = row,
                    rowValue = rowValue,
                    filter = "contains"
                )

                simplerSearch.fold(
                    onSuccess = {
                        Timber.tag("simplerSearch").d("received documents: $it")

                        if (it.isEmpty()) {
                            messageChannel.send("no documents found.")
                        } else {
                            messageChannel.send("${it.size} documents loaded successfully.")
                        }

                        delay(UITimeout)


                        _mutableState.value = _mutableState.value.copy(
                            isLoading = false,
                            documentsFromUploaded = it
                        )
                    },
                    onFailure = {
                        Timber.d("failed: ${it.message}")
                        messageChannel.send(it.message.toString())
                        delay(UITimeout)
                        _mutableState.value = _mutableState.value.copy(
                            isLoading = false,
                        )
                    }
                )

            } else {
                val documentsByQuery = documentRepo.loadUploadedDocumentsByQuery(query = query)

                documentsByQuery.fold(
                    onSuccess = {
                        Timber.tag("documentsByQuery").d("received documents: $it")
                        if (it.isEmpty()) {
                            messageChannel.send("no documents found.")
                        } else {
                            messageChannel.send("${it.size} documents loaded successfully.")
                        }
                        delay(UITimeout)
                        _mutableState.value = _mutableState.value.copy(
                            isLoading = false,
                            documentsFromUploaded = it
                        )
                    },
                    onFailure = {
                        Timber.tag("documentsByQuery").d("failed: ${it.message}")
                        messageChannel.send(it.message.toString())
                        delay(UITimeout)
                        _mutableState.value = _mutableState.value.copy(
                            isLoading = false,
                        )
                    }
                )


            }


        }


    }

    override fun onCleared() {
        super.onCleared()
        messageChannel.close()
    }

}