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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.App
import com.kdbrian.sage.ui.composables.ListItemPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetails() {


    var isFavorite by remember {
        mutableStateOf(false)
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
                            text = "Topic Details"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
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


                        IconButton(onClick = {
                            isFavorite = !isFavorite
                        }) {
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
                ListItemPreview()
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
        TopicDetails()
    }
}