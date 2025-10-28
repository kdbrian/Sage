package com.sage.library.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sage.library.ui.state.DocumentLibraryUiState
import com.sage.ui.composables.locals.LocalFontFamily
import com.sage.ui.theme.SageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentLibrary(
    documentLibraryUiState: DocumentLibraryUiState=DocumentLibraryUiState(),
    onQueryUpdate: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {

//    interface DocumentListReducer: Reducer<DocumentLibraryUiState, LibraryEvent, LibraryEffects> {
//        override fun reduce(
//            previous: DocumentLibraryUiState,
//            event: LibraryEvent
//        ): Pair<DocumentLibraryUiState, LibraryEffects?> {
//            when(event){
//                else -> {}
//            }
//        }
//    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Documents") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                OutlinedTextField(
                    value = documentLibraryUiState.query ?: "",
                    onValueChange = onQueryUpdate,
                    placeholder = {
                        Text(
                            text = "name or topic",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = LocalFontFamily.current
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            items(documentLibraryUiState.documents) { documentModel ->
                key(documentModel?.documentId) {
                    DocumentListItem(
                        documentTitle = documentModel?.documentName  ?: "",
                        documentSummary = documentModel?.summary ?: ""
                    )
                }
            }


        }


    }

}

@Preview
@Composable
private fun DocumentLibraryPrev() {
    SageTheme {
        DocumentLibrary()
    }
}