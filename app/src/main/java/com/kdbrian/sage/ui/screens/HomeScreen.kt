package com.kdbrian.sage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kdbrian.sage.R
import com.kdbrian.sage.ui.composables.CategoryItem
import com.kdbrian.sage.ui.composables.CircularProfileAvatar
import com.kdbrian.sage.ui.composables.RoundedInputField
import com.kdbrian.sage.ui.composables.TopicCard
import com.kdbrian.sage.ui.theme.SageTheme

@Composable
fun HomeScreen() {

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 3.dp,
            ) {
                Column(
                    modifier = Modifier.padding(4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        CircularProfileAvatar { }

                        Spacer(Modifier.padding(12.dp))

                        RoundedInputField(
                            placeholder = "Search",
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null
                                )
                            }
                        )

                    }

                    /*val scrollState = rememberScrollState()
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .padding(horizontal = 12.dp)
                            .horizontalScroll(scrollState),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CategoryItem(text = "Music")

                        CategoryItem(text = "Science")

                        CategoryItem(text = "Finance")

                        CategoryItem(text = "Lifestyle")

                        CategoryItem(text = "Sports")

                    }*/

                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(12.dp)
        ) {

            Text(
                text = "Explore",
                style = MaterialTheme.typography.titleLarge
            )

            LazyHorizontalGrid(
                rows = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(330.dp),
            ) {
                items(12) {
                    TopicCard(
                        modifier = Modifier.padding(4.dp),
                        text = buildAnnotatedString { append("Topic $it") }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Documents For You",
                style = MaterialTheme.typography.titleLarge
            )


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
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
                    Spacer(Modifier.weight(1f))

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
                            }
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

@Preview
@Composable
private fun HomeScreenPrev() {
    SageTheme {
        HomeScreen()
    }
}
