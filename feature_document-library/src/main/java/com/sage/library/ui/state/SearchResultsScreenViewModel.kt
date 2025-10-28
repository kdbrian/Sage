package com.sage.library.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sage.domain.domain.model.DocumentModel
import com.sage.domain.domain.usecases.ListDocumentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchResultsScreenViewModel(
    private val listDocumentsUseCase: ListDocumentsUseCase
) : ViewModel() {

    private val _mutableState = MutableStateFlow(SearchResultsUiState())
    val uiState = _mutableState.asStateFlow()

    private var _listProducts: List<DocumentModel?>? = null


    fun loadDocuments() {
        viewModelScope.launch {
            _mutableState.update {
                it.copy(
                    isLoading = true,
                )
            }

            listDocumentsUseCase
                .invoke(Unit)
                .collect { documentModels ->
//                            isLoading = false,
//                            documentsFromDefault = documentModels
                }
        }
    }

    fun loadSearchResults(query: String) {
        viewModelScope.launch {
            _mutableState.value = _mutableState.value.copy(
                query = query,
                documentsFromDefault = _listProducts?.filter {
                    it?.documentName?.contains(query, true) == true ||
                            it?.summary?.contains(query, true) == true
                }.orEmpty()
            )

        }


    }

    override fun onCleared() {
        super.onCleared()
        _mutableState.value = SearchResultsUiState()
        loadDocuments()
    }

}

data class SearchResultsUiState(
    val isLoading: Boolean = false,
    val query: String? = null,
    val documentsFromDefault: List<DocumentModel?> = emptyList(),
)