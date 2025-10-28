package com.sage.feature_upload

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sage.ui.composables.CategoryItem
import com.sage.ui.composables.RoundedInputField
import com.sage.ui.composables.RowButton
import com.sage.ui.theme.SageTheme
import com.sage.ui.util.FileUtils.getFileName
import com.sage.ui.util.Utils.displayToast
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    uiState: CreateScreenUiState = CreateScreenUiState(),
    updateUri: (Uri?) -> Unit = {},
    updateName: (String) -> Unit = {},
    updateSummary: (String) -> Unit = {},
    addTopic: (String) -> Unit = {},
    removeTopic: (String) -> Unit = {},
    uploadDocument: () -> Unit = {},
    validate: () -> Boolean = { false },
) {

    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    var isAddingTopic by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(isAddingTopic) {
        if (!isAddingTopic) {
            bottomSheetState.hide()
        } else {
            bottomSheetState.show()
        }
    }
    val pickDocument =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
            it?.let {
                context.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                updateUri(it)
            }
        }
    val iconAndText by remember {
        derivedStateOf {
            if (uiState.docUri != Uri.EMPTY && uiState.docUri != null) {
                val fileName = context.getFileName(uiState.docUri!!)
                fileName?.let {
                    val updatedName = if (it.endsWith(".pdf", true)) it.removeSuffix(".pdf")
                    else if (it.endsWith(".docx", true)) it.removeSuffix(".docx")
                    else it
                    updatedName to Icons.Rounded.Upload
                } ?: ("" to Icons.Rounded.Add)
            } else {
                "Select Document." to Icons.Rounded.Add
            }
        }
    }
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current


    //display message

    val focusManager = LocalFocusManager.current


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = buildAnnotatedString {

                        withStyle(
                            SpanStyle(
                                fontSize = MaterialTheme.typography.displayLarge.fontSize,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue.copy(alpha = 0.65f)
                            )
                        ) {
                            append("C")
                        }



                        withStyle(
                            SpanStyle(
                                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                                fontWeight = FontWeight.Bold,
                                baselineShift = BaselineShift.Superscript,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("reate\n")
                        }


                        withStyle(
                            SpanStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontStyle = FontStyle.Italic,
                            )
                        ) {
                            append("Documents that create ")
                            append("experiences.\n")
                        }


                        withStyle(
                            SpanStyle(
                                fontSize = 12.sp,
                                color = Color.LightGray,
                                fontWeight = FontWeight.SemiBold,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append("Allowed file types include ebooks, pdf & word\n")
                        }

                        withStyle(
                            SpanStyle(
                                fontSize = 8.sp,
                                color = Color.Red,
                                fontWeight = FontWeight.Light,
                            )
                        ) {
                            append("Make sure you have the read terms and conditions section on file uploads.")
                        }
                    },
                    style = TextStyle(
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.widthIn(max = 280.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            RowButton(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Row {
                        Icon(
                            imageVector = iconAndText.second,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = iconAndText.first ?: "Add Document",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                onClick = {
                    if (!uiState.isUploading)
                        pickDocument.launch(
                            arrayOf(
                                "application/pdf",
                                "application/msword",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                            )
                        )
                },
                trailingIcon = {
                    AnimatedVisibility(uiState.docUri != null && uiState.docUri != Uri.EMPTY) {


                        IconButton(onClick = {
                            clipboardManager.setText(
                                buildAnnotatedString {
                                    append(iconAndText.first)
                                }
                            )

                            updateName(iconAndText.first)

                        }) {
                            Icon(imageVector = Icons.Rounded.CopyAll, contentDescription = null)
                        }
                    }
                }
            )

            RoundedInputField(
                enabled = !uiState.isUploading,
                fieldState = uiState.documentName,
                onValueChange = updateName,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyBoardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                placeholder = "rename document"
            )

            RoundedInputField(
                supportText = {
                    Text(text = "be brief.", style = MaterialTheme.typography.labelSmall)
                },
                enabled = !uiState.isUploading,
                onValueChange = updateSummary,
                fieldState = uiState.documentSummary,
                singleLine = false,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Summary",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyBoardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = "Topics", style = MaterialTheme.typography.titleLarge)

                    TextButton(onClick = {
                        if (!uiState.isUploading) {
                            if (uiState.docUri == Uri.EMPTY || uiState.docUri == null) {
                                context.displayToast(
                                    message = "Add a document first",
                                    apply = {
                                        setDuration(Toast.LENGTH_LONG)
                                    }
                                )

                            } else isAddingTopic = !isAddingTopic
                        }
                    }) {
                        Text(text = "Add")
                    }
                }

                LazyHorizontalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(4.dp),
                    rows = GridCells.Fixed(2),
                ) {
                    Timber.d("uiState.topics.size : ${uiState.topics.size}")
                    itemsIndexed(uiState.topics.toList()) { index, topic ->
                        key(index) {
                            CategoryItem(
                                text = topic, modifier = Modifier.padding(4.dp),
                                onClick = {
                                    clipboardManager.setText(
                                        buildAnnotatedString {
                                            append(topic)
                                        }
                                    )
                                    context.displayToast(
                                        message = "Copied to clipboard.",
                                    )
                                    removeTopic(topic)
                                }
                            )
                        }
                    }
                }
            }


        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            visible = uiState.docUri != Uri.EMPTY && uiState.docUri != null && !uiState.isUploading
        ) {
            Button(
                onClick = {
                    if (validate()) {
                        uploadDocument()
                    }
                },
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Upload")
            }

        }


        AnimatedVisibility(uiState.isUploading, modifier = Modifier.align(Alignment.BottomCenter)) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp
                    )
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }


        }

        if (isAddingTopic) {
            val topicName = rememberTextFieldState()

            ModalBottomSheet(
                onDismissRequest = {
                    isAddingTopic = false
                }, sheetState = bottomSheetState
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = "Add Topic",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    RoundedInputField(
                        fieldState = topicName,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Topic Name"
                    )

                    RowButton(
                        leadingIcon = { Text("Save") },
                        modifier = Modifier.padding(top = 12.dp),
                        onClick = {
                            addTopic(topicName.text.trim().toString())
                            topicName.clearText()
                        })
                }
            }
        }

    }
}

@Preview
@Composable
private fun CreateScreenPrev() {
    SageTheme {
        CreateScreen()
    }
}