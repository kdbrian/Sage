package com.sage.library.ui.state

import androidx.lifecycle.viewModelScope
import com.sage.core.BaseViewModel
import com.sage.core.Reducer
import com.sage.domain.domain.model.DocumentModel
import com.sage.domain.domain.usecases.ListDocumentsUseCase
import com.sage.library.ui.event.LibraryEffects
import com.sage.library.ui.event.LibraryEvent
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class DocumentLibraryUiState(
    val query: String? = null,
    val documents: List<DocumentModel?> = emptyList()
) : Reducer.UiState

class DocumentLibraryViewModel(
    private val listDocumentsUseCase: ListDocumentsUseCase, initialState: DocumentLibraryUiState,
    reducer: Reducer<DocumentLibraryUiState, LibraryEvent, LibraryEffects>
) : BaseViewModel<DocumentLibraryUiState, LibraryEvent, LibraryEffects>(initialState, reducer) {


    private val _cacheList: List<DocumentModel?>? = null

    init {
        viewModelScope.launch {
            loadDocuments()
        }
    }

    private suspend fun loadDocuments() {
        listDocumentsUseCase
            .invoke(Unit)
            .stateIn(viewModelScope)
            .collect { documentModels ->
            }
    }

    fun onQueryUpdate(newQuery: String?) {
        viewModelScope.launch {
//                    query = newQuery,
//                    documents = _cacheList?.filter { it?.documentName?.contains(newQuery.toString(), true) == true }?.ifEmpty { _cacheList } ?: emptyList()
        }
    }


}