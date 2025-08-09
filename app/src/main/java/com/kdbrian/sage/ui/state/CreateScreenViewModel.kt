package com.kdbrian.sage.ui.state

import android.content.Context
import android.net.Uri
import android.view.textclassifier.ConversationActions
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.sage.domain.repo.DocumentRepo
import com.kdbrian.sage.ui.screens.CreateScreen
import com.kdbrian.sage.util.FileUtils.getDocumentMetadataMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import kotlinx.coroutines.delay

class CreateScreenViewModel(
    private val context: Context,
    private val documentRepo: DocumentRepo,
) : ViewModel() {


    private val _mutableState = MutableStateFlow(CreateScreenUiState())
    val uiState = _mutableState.asStateFlow()

    private val messageChannel = Channel<String>()
    val messages = messageChannel.receiveAsFlow()

    private val MessageUpdateTimeout = 3500L


    fun validate(): Boolean {

        val coroutineScope = CoroutineScope(viewModelScope.coroutineContext)

        //doc uri != null || Uri.Empty
        if (_mutableState.value.docUri == Uri.EMPTY || _mutableState.value.docUri == null) {
            coroutineScope.launch {
                messageChannel.send("Add a document first.")
            }
            return false
        }

        //docName not empty
        if (_mutableState.value.documentName.text.isEmpty()) {
            coroutineScope.launch {
                messageChannel.send("Document name cannot be empty.")
            }
            return false
        }

        //summary not < 150 words
        if (_mutableState.value.documentSummary.text.toString().length < 200) {
            coroutineScope.launch {
                messageChannel.send("Summary should be at least 200 letters.")
            }
            return false
        }


        //topics >= 4
        if (_mutableState.value.topics.size < 4) {
            coroutineScope.launch {
                messageChannel.send("Topics should be at least 4.")
            }
            return false
        }

        return true

    }

    fun uploadDocument() {
        viewModelScope.launch {
            _mutableState.value= _mutableState.value.copy(isUploading = true)
            messageChannel.send("validating document...")

            if (validate()) {

                messageChannel.send("uploading document...")
                val documentModel = _mutableState.value.copyDocumentModel().apply {
                        fileInfo = context.getDocumentMetadataMap(uri.toUri()).toString()
                    }


                val result = documentRepo.saveDocumentMetaData(
                    uri = _mutableState.value.docUri!!,
                    documentModel = documentModel
                )

                result.onSuccess {
                    messageChannel.send("document uploaded successfully.")
                    delay(MessageUpdateTimeout)
                    _mutableState.value = CreateScreenUiState()
                }

                result.onFailure {
                    messageChannel.send("document uploaded  failed ${it.message}")
                    delay(MessageUpdateTimeout)
                    _mutableState.value = _mutableState.value.copy(isUploading = false)
                }

            }else
                messageChannel.send("Fill all fields and retry.")
        }
    }

    fun updateName(name: String) {
        _mutableState.value.documentName.setTextAndPlaceCursorAtEnd(name)
    }

    fun updateSummary(summary: String) {
        _mutableState.value.documentSummary.setTextAndPlaceCursorAtEnd(summary)
    }

    fun addTopic(topic: String) {
        _mutableState.value = _mutableState.value.copy(
            topics = _mutableState.value.topics + topic
        )
    }

    fun removeTopic(topic: String) {
        _mutableState.value = _mutableState.value.copy(
            topics = _mutableState.value.topics - topic
        )
    }

    fun updateUri(uri: Uri) {
        _mutableState.value = _mutableState.value.copy(
            docUri = uri,

            )

    }


    override fun onCleared() {
        super.onCleared()
        clear()
    }

    fun clear() {
        _mutableState.value = CreateScreenUiState()
    }


}