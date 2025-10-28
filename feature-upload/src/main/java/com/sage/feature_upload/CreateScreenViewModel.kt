package com.sage.feature_upload

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.sage.domain.repo.DocumentRepo
import com.kdbrian.sage.ui.util.UiTimeOut
import com.sage.ui.util.FileUtils.getDocumentMetadataMap
import com.sage.ui.util.NotificationData
import com.sage.ui.util.NotificationUtils.showNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

class CreateScreenViewModel(
    private val context: Context,
    private val documentRepo: DocumentRepo,
) : ViewModel() {


    private val _mutableState = MutableStateFlow(CreateScreenUiState())
    val uiState = _mutableState.asStateFlow()

    private val messageChannel = Channel<String>()
    val messages = messageChannel.receiveAsFlow()


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

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
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

                result.onSuccess  {
                    messageChannel.send("document uploaded successfully.")
                    context.showNotification(
                        data = NotificationData(
                            channelId = context.getString(R.string.default_notification_channel),
                            title = "Upload task",
                            message = "${documentModel.documentName} uploaded successfully at ${Date(documentModel.publicationAt)}",
                            smallIcon = R.drawable.baseline_check_24
                        ),
                        notificationId = Random.nextInt(1,9)
                    )
                    delay(UiTimeOut.timeOut)
                    _mutableState.value = CreateScreenUiState()
                }

                result.onFailure {
                    messageChannel.send("document uploaded  failed ${it.message}")
                    delay(UiTimeOut.timeOut)
                    _mutableState.value = _mutableState.value.copy(isUploading = false)
                    context.showNotification(
                        data = NotificationData(
                            channelId = context.getString(R.string.default_notification_channel),
                            title = "Upload task",
                            message = "${documentModel.documentName} upload failed at ${Date(documentModel.publicationAt)}",
                            smallIcon = R.drawable.outline_brightness_alert_24
                        ),
                        notificationId = Random.nextInt(1,9)
                    )
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
        messageChannel.close()
    }


}

data class CreateScreenUiState(
    val isUploading: Boolean = false,
    val docUri: Uri? = null,
    val documentName: TextFieldState = TextFieldState(),
    val documentSummary: TextFieldState = TextFieldState(),
    val topics: Set<String> = mutableSetOf()
)

fun CreateScreenUiState.copyDocumentModel() =
    DocumentModel(
        documentName = documentName.text.trim().toString(),
        summary = documentSummary.text.trim().toString(),
        topics = topics.toList(),
        uri = docUri.toString()
    )