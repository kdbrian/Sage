package com.kdbrian.sage.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.sage.domain.repo.DocumentRepo
import com.kdbrian.sage.ui.screens.DocumentDetailsUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class DocumentDetailsScreenViewModel(
    private val documentRepo: DocumentRepo
) : ViewModel() {


    private val _mutableState = MutableStateFlow(DocumentDetailsUiState())
    val uiState = _mutableState.asStateFlow()

    private val messageChannel = Channel<String>()
    val messages = messageChannel.receiveAsFlow()


    fun loadDocumentById(documentId: String) {
        viewModelScope.launch {
            _mutableState.value = _mutableState.value.copy(isLoading = true)

            val simplerSearch = documentRepo.simplerSearch(
                row = "documentId",
                rowValue = documentId,
                filter = "equals"
            )

            simplerSearch.fold(
                onSuccess = {
                    _mutableState.value = _mutableState.value.copy(
                        documentModel = it.first()
                    )
                },
                onFailure = {
                    Timber.d("failed: ${it.message}")
                }
            )
        }


    }


}