package com.sage.library.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Copyright
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sage.library.ui.state.DocumentDetailsUiState
import com.sage.ui.R
import com.sage.ui.theme.SageTheme
import com.sage.ui.util.DateUtils.formatAsDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetails(
    uiState: DocumentDetailsUiState = DocumentDetailsUiState(),
    onBack: () -> Unit = {},
) {

    var isFavorite by remember {
        mutableStateOf(false)
    }

    val favColor by animateColorAsState(
        if (isFavorite)
            Color.Red
        else
            Color.Black
    )

    val size = animateDpAsState(
        if (isFavorite)
            25.dp
        else
            18.dp
    )

    var index by remember {
        mutableIntStateOf(0)
    }

//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(UiTimeOut.timeOut)
//            if (index == (uiState.documentModel?.topics?.size ?: 0) - 1) {
//                index--
//            } else {
//                index++
//            }
//        }
//    }


    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 3.dp
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = uiState.documentModel?.topics[index] ?: "",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clickable {
//                                        navHostController.navigate(
//                                            SearchResultsRoute(query = "topic:${uiState.documentModel?.topics[index]}")
//                                        )
                                    }
                            )
                        }
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
                        IconButton(onClick = {
                            isFavorite = !isFavorite
                        }) {
                            Icon(
                                tint = favColor,
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(size.value)
                            )
                        }
                    },
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {


            Text(
                text = uiState.documentModel?.documentName ?: "",
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.padding(12.dp))

            BasicText(
                text = uiState.documentModel?.summary ?: "",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 12,
                overflow = TextOverflow.Ellipsis
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 2.dp
            ) {

                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {

                        Text(
                            text = "Rights", style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Icon(
                            imageVector = Icons.Rounded.Copyright,
                            modifier = Modifier.size(25.dp),
                            contentDescription = null
                        )

                    }

                    Text(
                        text = "u#${uiState.documentModel?.publisher}",
                        style = MaterialTheme.typography.bodyMedium
                    )


                    Text(
                        text = (uiState.documentModel?.publicationAt?.toLong()
                            ?: System.currentTimeMillis()).formatAsDate(),
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 5.dp
            ) {

                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {

                        Text(
                            text = "Summary", style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Icon(
                            painter = painterResource(R.drawable.stars_2_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
                            modifier = Modifier.size(25.dp),
                            contentDescription = null
                        )

                    }

                    Text(
                        text = uiState.documentModel?.aiSummary ?: "Still building.....",
                        style = MaterialTheme.typography.bodyMedium
                    )


                    IconButton(
                        modifier = Modifier.align(Alignment.End), onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.FastForward,
                            contentDescription = null,
                        )
                    }

                }
            }

            Spacer(Modifier.weight(1f))


            TextButton(onClick = {}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Row {
                    Text(text = "Share")
                }
            }

            Spacer(Modifier.weight(1f))


        }
    }

}

@Preview
@Composable
private fun DocumentDetailsPrev() {
    SageTheme {
        DocumentDetails()
    }
}