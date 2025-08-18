package com.kdbrian.sage.ui.screens

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kdbrian.sage.App
import com.kdbrian.sage.nav.DocumentDetailsRoute
import com.kdbrian.sage.ui.composables.ListItemPreview
import com.kdbrian.sage.ui.state.SearchResultsScreenViewModel
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResults(
    query: String = LoremIpsum(2).values.joinToString(),
    searchResultsScreenViewModel: SearchResultsScreenViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiState by searchResultsScreenViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val messages by searchResultsScreenViewModel.messages.collectAsState("")

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            snackbarHostState.showSnackbar(messages)
        }
    }



    Scaffold(
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(hostState = snackbarHostState)
        },
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
                                    append("for $query")
                                }
                            },
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
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

                Timber.d("document: ${uiState.documentsFromUploaded.size}")

                itemsIndexed(uiState.documentsFromUploaded) { index, document ->
                    key(index) {
                        ListItemPreview(
                            title = document?.documentName ?: "",
                            subtitle = buildAnnotatedString {
                                append(document?.topics?.joinToString(", "))
                            },
                            onClick = {
                                document?.documentId?.let {
                                    navController.navigate(DocumentDetailsRoute(it))
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
    App {
        SearchResults()
    }
}