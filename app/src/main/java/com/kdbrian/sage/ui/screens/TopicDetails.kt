package com.kdbrian.sage.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import com.kdbrian.sage.App
import com.kdbrian.sage.nav.DocumentDetailsRoute
import com.kdbrian.sage.ui.composables.ListItemPreview
import com.kdbrian.sage.ui.state.TopicDetailsUiState
import com.kdbrian.sage.ui.state.TopicDetailsViewModel
import com.kdbrian.sage.util.AppDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetails(
    topicDetailsViewModel: TopicDetailsViewModel = koinViewModel(),
    navController: NavHostController,
    onClose: () -> Unit = {}
) {

    val uiState by topicDetailsViewModel.uiState.collectAsState()

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

                        IconButton(onClick = topicDetailsViewModel::onFavourite) {
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
            items(5) {
                ListItemPreview(
                    onClick = {
                        navController.navigate(
                            DocumentDetailsRoute(
                                UUID.randomUUID().toString().split("-").first()
                            )
                        )
                    }
                )
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
    App {
//        TopicDetails()
    }
}