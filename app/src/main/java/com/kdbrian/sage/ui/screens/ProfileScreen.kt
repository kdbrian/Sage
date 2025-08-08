package com.kdbrian.sage.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.App
import com.kdbrian.sage.BuildConfig
import com.kdbrian.sage.ui.composables.CategoryItem
import com.kdbrian.sage.ui.composables.CircularProfileAvatar
import com.kdbrian.sage.ui.composables.RowButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 2.dp
            ) {
                TopAppBar(
                    title = { Text(text = "Profile & Settings.") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
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
                .padding(16.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    CircularProfileAvatar(
                        size = 100,
                    ) { }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                            .padding(6.dp)
                    ) {
                        Text(text = "Joe Doe", style = MaterialTheme.typography.displaySmall)
                        Text(
                            text = "joedoe@dev.to",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Light
                            )
                        )


                        val scrollState = rememberScrollState()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 12.dp)
                                .horizontalScroll(scrollState)
                        ){

                            CategoryItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Info,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp)
                                    )
                                },
                                color = Color(0xFFF7F0F0),
                                text = "Sage points",
                                modifier = Modifier
                            )

                            CategoryItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.MusicNote,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp)
                                    )
                                },
                                color = Color(0xFFF7F0F0),
                                text = "Profile Tune",
                                modifier = Modifier
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "Profile", style = MaterialTheme.typography.titleLarge)

                    RowButton(
                        leadingIcon = {
                            Text(
                                "Activity",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(imageVector = Icons.Rounded.Schedule, contentDescription = null)
                        }
                    ) { }

                    RowButton(
                        leadingIcon = {
                            Text(
                                "Saved",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(imageVector = Icons.Rounded.Bookmark, contentDescription = null)
                        }
                    ) { }

                    RowButton(
                        leadingIcon = {
                            Text(
                                "Favourite",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(imageVector = Icons.Rounded.Favorite, contentDescription = null)
                        }
                    ) { }
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 3.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Become A Sage Master",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Icon(
                                    imageVector = Icons.Rounded.Verified,
                                    contentDescription = null,
                                    tint = Color.Blue
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sage Master",
                                    style = MaterialTheme.typography.labelMedium
                                )


                                Text(
                                    text = "$0.30/hr",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Light
                                    )
                                )


                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "god Mod",
                                    style = MaterialTheme.typography.labelMedium
                                )


                                Text(
                                    text = "$30",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Light
                                    )
                                )


                            }

                        }
                    }


                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                    )

                }
            }


            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = "Preferences", style = MaterialTheme.typography.titleLarge
                    )

                    RowButton(
                        leadingIcon = {
                            Text(
                                "Notification settings",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = null
                            )
                        }
                    ) { }


                    TextButton(onClick = {}) {
                        Text(
                            text = "Delete Account",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.Red,
                            )
                        )
                    }

                    TextButton(onClick = {}) {
                        Text(
                            text = "Terms & Conditions",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.Blue
                            )
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "App ${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(Modifier.weight(1f))

                }
            }


        }
    }


}

@Preview
@Composable
private fun ProfileScreenPrev() {
    App {
        ProfileScreen()
    }
}