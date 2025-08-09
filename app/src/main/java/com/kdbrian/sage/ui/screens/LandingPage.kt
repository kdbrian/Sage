package com.kdbrian.sage.ui.screens

import android.icu.text.StringSearch
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.sage.App
import com.kdbrian.sage.R
import com.kdbrian.sage.ui.composables.CategoryItem
import com.kdbrian.sage.ui.composables.CircularProfileAvatar
import com.kdbrian.sage.ui.composables.RoundedInputField
import com.kdbrian.sage.ui.composables.TopicCard
import com.kdbrian.sage.ui.state.HomeScreenUiState
import com.kdbrian.sage.util.AppDataStore
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(
    uiState: HomeScreenUiState,
    onOpenProfile: () -> Unit = {},
    onOpenTopic: (String) -> Unit = {},
    openFYP: () -> Unit = {}
) {

    val appDataStore = koinInject<AppDataStore>()

    var isExpanded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        isExpanded = appDataStore.firstTime.first()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Surface {
                TopAppBar(
                    modifier = Modifier,
                    title = {
                        BasicText(
                            text = "Hello,",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            autoSize = TextAutoSize.StepBased(
                                maxFontSize = 36.sp,
                                minFontSize = 32.sp,
                                stepSize = (2.75).sp,
                            ),
                            style = MaterialTheme.typography.displaySmall,
                        )
                    },
                    navigationIcon = {
                        CircularProfileAvatar(
                            modifier = Modifier.padding(8.dp),
                            onImageClick = onOpenProfile
                        )
                    },
                    actions = {
                        AnimatedContent(
                            targetState = isExpanded,
                            modifier = Modifier.padding(6.dp)
                        ) {

                            if (!it) {
                                Surface(
                                    shape = CircleShape,
                                    shadowElevation = 3.dp,
                                    onClick = { isExpanded = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                        contentDescription = null,
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        shadowElevation = 3.dp,
                                        onClick = { isExpanded = false }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = null
                                        )
                                    }

                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Rounded.Schedule,
                                            contentDescription = null
                                        )
                                    }

                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Rounded.Favorite,
//                                            tint = Color.Red,
                                            contentDescription = null
                                        )
                                    }

                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Rounded.BookmarkAdded,
//                                            tint = Color.Green,
                                            contentDescription = null
                                        )
                                    }
                                }


                            }


                        }


                    }
                )
            }
        }
    ) { pd ->

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(pd)
                .fillMaxSize()
        ) {

            Column {

                Text(
                    text = "Explore",
                    modifier = Modifier.padding(top = 12.dp, start = 12.dp),
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Bold)
                )

                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 12.dp)
                        .requiredHeightIn(max = 180.dp),
                ) {
                    if (uiState.isLoading) {
                        items(12) {
                            TopicCard(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.LightGray),
                                text = buildAnnotatedString { append(" ") },
                            )
                        }
                    } else {
                        itemsIndexed(uiState.topics) { index, topic ->
                            key(index) {
                                TopicCard(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    text = buildAnnotatedString {
                                        append(topic?.topicName ?: "")
                                    },
                                    onClick = {
                                        onOpenTopic(topic?.topicId ?: "Invalid")
                                    }
                                )
                            }
                        }
                    }
                }
            }

//            Spacer(Modifier.weight(1f))

            RoundedInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                fieldState = uiState.searchQuery,
                placeholder = "Search",
            )

//            Spacer(Modifier.weight(1f))

            Column {

                Text(
                    text = "Documents For You",
                    modifier = Modifier.padding(top = 12.dp, start = 12.dp),
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 12.dp)
                        .requiredHeightIn(
                            max = 200.dp,
                            min = 180.dp
                        ),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 3.dp,
                ) {

                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        model = null,
                        error = painterResource(R.drawable.paper_avalanche),
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black//.copy(alpha = 0.85f)
                                    )
                                )
                            ),
                        content = {},
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
//                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "For You",
                            style = MaterialTheme.typography.displaySmall
                        )


                        Text(
                            text = "A collection of what you may like. We keep it updated with every new document you like",
                            style = MaterialTheme.typography.labelMedium.copy(
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Light
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CategoryItem(
                                text = "explore",
                                icon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                },
                                onClick = openFYP
                            )

                            TextButton(onClick = {}) {
                                Text(text = "Shared Documents")
                            }
                        }


                    }


                }
            }


        }
    }
}

@Preview
@Composable
private fun LandingPagePrev() {
    App {
        LandingPage(
            uiState = HomeScreenUiState()
        )
    }
}