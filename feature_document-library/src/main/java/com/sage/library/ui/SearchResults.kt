package com.sage.library.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sage.library.ui.state.SearchResultsUiState
import com.sage.ui.composables.ListItemPreview
import com.sage.ui.theme.SageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResults(
    uiState: SearchResultsUiState = SearchResultsUiState(),
    onBack: () -> Unit = {},
    onDocumentExpand: (String) -> Unit = {},
) {


    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 3.dp
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                ) {
                                    append("SearchResults\n")
                                }

                                withStyle(
                                    SpanStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Light,
                                        color = Color.LightGray
                                    )
                                ) {
                                    append("for $uiState.")
                                }
                            },
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Rounded.Save,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    ) { pd ->
        LazyColumn(
            modifier = Modifier
                .padding(pd)
                .padding(12.dp)
        ) {
            if (uiState.isLoading) {
                items(5) {
                    ListItemPreview()
                }
            } else {


                itemsIndexed(uiState.documentsFromDefault) { index, document ->
                    key(index) {
                        ListItemPreview(
                            title = document?.documentName ?: "",
                            subtitle = buildAnnotatedString {
                                append(document?.topics?.joinToString(", "))
                            },
                            onClick = {
                                document?.documentId?.let {
                                    onDocumentExpand(it)
                                }
                            }
                        )
                    }
                }
            }

            item {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 24.dp,
                                vertical = 6.dp
                            )
                        )

                        TextButton(onClick = {}) {
                            Text(text = "More", style = MaterialTheme.typography.titleMedium)
                        }

                    }
                }
            }
        }
    }


}


@Preview
@Composable
private fun SearchResultsPrev() {
    SageTheme {
        SearchResults()
    }
}