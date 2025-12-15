package com.kdbrian.sage.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.sage.App
import com.kdbrian.sage.R
import com.kdbrian.sage.ui.state.HomeScreenUiState
import com.sage.datastore.AppDataStore
import com.sage.ui.composables.CategoryItem
import com.sage.ui.composables.CircularProfileAvatar
import com.sage.ui.composables.RoundedInputField
import com.sage.ui.composables.TopicCard
import com.sage.ui.composables.locals.LocalFontFamily
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(
    uiState: HomeScreenUiState,
    onSearch: (String) -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onOpenTopic: (String) -> Unit = {},
    openFYP: () -> Unit = {}
) {

//    val appDataStore = koinInject<AppDataStore>()

    var isExpanded by remember {
        mutableStateOf(false)
    }
//    LaunchedEffect(Unit) {
//        isExpanded = appDataStore.firstTime()
//    }

    val focusManager = LocalFocusManager.current
    Scaffold { pd ->

        Column(
//            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(pd)
                .fillMaxSize(),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Hi,",
                    modifier = Modifier,
                    style = MaterialTheme.typography.displaySmall
                        .copy(fontWeight = FontWeight.Bold, fontFamily = LocalFontFamily.current)
                )

                Text(
                    text = "Documents For You",
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyLarge
                        .copy(fontWeight = FontWeight.Bold, fontFamily = LocalFontFamily.current)
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors()
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        ),
                        color = Color.White
                    )
                    .padding(12.dp)
            ) {

                Text(
                    text = "Explore Topics",
                    modifier = Modifier.padding(top = 12.dp, start = 12.dp),
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Bold)
                )

                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 12.dp)
                        .requiredHeightIn(max = 250.dp),
                ) {
                    if (uiState.isLoading) {
                        items(20) {
                            TopicCard(
                                modifier = Modifier
                                    .width(100.dp)
                                    .padding(4.dp),
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