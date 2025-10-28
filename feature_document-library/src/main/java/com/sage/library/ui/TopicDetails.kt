package com.sage.library.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sage.library.ui.state.TopicDetailsUiState
import com.sage.ui.composables.ListItemPreview
import com.sage.ui.theme.SageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetails(
    uiState: TopicDetailsUiState = TopicDetailsUiState(),
    onFavourite: () -> Unit = {},
    onExpandDocument: (String) -> Unit = {},
    onClose: () -> Unit = {},
) {


    val isFavorite by remember {
        derivedStateOf {
            uiState.favouriteTopics.any {
                it == uiState.topicDetails?.topicId
            }
        }
    }

    val icon = remember {
        derivedStateOf {
            if (isFavorite)
                Icons.Rounded.Favorite
            else
                Icons.Rounded.FavoriteBorder
        }
    }


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
                                    append("Topic Details\n")
                                }

                                withStyle(
                                    SpanStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Light,
                                        color = Color.LightGray
                                    )
                                ) {
                                    append(uiState.topicDetails?.topicName ?: "")
                                }
                            },
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        val tint by animateColorAsState(
                            if (isFavorite)
                                Color.Red
                            else
                                Color.Black
                        )

                        IconButton(onClick = onFavourite) {
                            Icon(
                                tint = tint,
                                imageVector = icon.value,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(12.dp)
        ) {
            if (uiState.isLoading) {
                items(3) {
                    ListItemPreview()
                }
            } else {
//                uiState.topicDocuments?.let { documentModels ->
                itemsIndexed(uiState.topicDocuments) { index, model ->
                    key(index) {
                        model?.let { m ->
                            ListItemPreview(
                                title = m.documentName,
                                onClick = {
                                    onExpandDocument(m.documentId)
                                }
                            )
                        }
                    }
                }
//                }
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
private fun TopicDetailsPrev() {
    SageTheme {
        TopicDetails()
    }
}