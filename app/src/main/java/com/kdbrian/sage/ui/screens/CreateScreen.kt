package com.kdbrian.sage.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.ui.composables.CategoryItem
import com.kdbrian.sage.ui.composables.RoundedInputField
import com.kdbrian.sage.ui.composables.RowButton
import com.kdbrian.sage.ui.theme.SageTheme
import com.kdbrian.sage.util.FileUtils.getFileName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(modifier: Modifier = Modifier) {

    val coroutineScope = rememberCoroutineScope()
    var docUri by remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }
    val context = LocalContext.current

    val bottomSheetState = rememberModalBottomSheetState()
    var isAddingTopic by remember {
        mutableStateOf(false)
    }

    val topics = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(isAddingTopic) {
        if (!isAddingTopic) {
            bottomSheetState.hide()
        } else {
            bottomSheetState.show()
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    val pickDocument = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        it?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            docUri = it
        }
    }

    val iconAndText by remember {
        derivedStateOf {
            if (docUri != Uri.EMPTY && docUri != null) {
                context.getFileName(docUri!!) to Icons.Rounded.Upload
            } else {
                "Select Document." to Icons.Rounded.Add
            }
        }
    }



    Scaffold(
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Text(text = "Create", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = LoremIpsum(12).values.joinToString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                )

            }

            Spacer(Modifier.height(24.dp))

            RowButton(
                modifier = Modifier
                    .fillMaxWidth(),
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
                    pickDocument.launch(
                        arrayOf(
                            "application/pdf",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        )
                    )
                }
            )

            RoundedInputField(
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = "rename document"
            )

            RoundedInputField(
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = "Summary"
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


                    CategoryItem(text = "Add", onClick = {
                        if (docUri == Uri.EMPTY || docUri == null) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please select a document")
                            }
                        } else
                            isAddingTopic = !isAddingTopic
                    })
                }


            }

            LazyHorizontalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(4.dp),
                rows = GridCells.Fixed(3),
            ) {
                itemsIndexed(topics) { index, topic ->
                    key(index) {
                        CategoryItem(
                            text = topic,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

        }

        if (isAddingTopic) {
            val topicName = rememberTextFieldState()

            ModalBottomSheet(
                onDismissRequest = {
                    isAddingTopic = false
                },
                sheetState = bottomSheetState
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
                            if (topics.size == 8) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("You can only add 8 topics.")
                                }
                            } else {
                                topics.add(topicName.text.trim().toString())
                            }
                            isAddingTopic = false
                        }
                    )
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