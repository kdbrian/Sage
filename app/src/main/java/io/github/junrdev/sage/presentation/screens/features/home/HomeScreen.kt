package io.github.junrdev.sage.presentation.screens.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.junrdev.sage.R
import io.github.junrdev.sage.ui.composables.FeatureItem
import io.github.junrdev.sage.ui.composables.FeatureItemModel
import io.github.junrdev.sage.ui.theme.SageTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val horizontalHomeScrollState = rememberScrollState()

    var searchQuery by remember { mutableStateOf("") }
    val onSearch : () -> Unit = {}


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            HomeTopBar(modifier = Modifier.padding(8.dp))
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(12.dp)
                .verticalScroll(state = horizontalHomeScrollState)
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {

                Image(
                    painter = painterResource(R.drawable.reading_illustration),
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.BottomEnd),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth(0.5f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Are you ready for a ",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Breath taking experience",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = "buckle up ", style = MaterialTheme.typography.bodySmall)
                }


                Card(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.Yellow),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Upload new", modifier = Modifier.padding(12.dp))
                }


            }

            Spacer(Modifier.height(16.dp))

            Text(text = "Explore", style = MaterialTheme.typography.titleLarge)

            val demoFeatures = listOf(
                FeatureItemModel(),
                FeatureItemModel(),
                FeatureItemModel(),
                FeatureItemModel(),
                FeatureItemModel(),
            )

            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(items = demoFeatures) {
                    FeatureItem(featureItemModel = it)
                }
            }


            //search bar
            Spacer(Modifier.height(16.dp))
            HomeSearchBar(searchQuery, onSearch)


            //display saves
            Spacer(Modifier.height(16.dp))

            Text(text = "Your History", style = MaterialTheme.typography.titleLarge)


        }

    }
}

@Composable
private fun HomeSearchBar(searchQuery: String, onSearch: () -> Unit) {
    var searchQuery1 = searchQuery
    TextField(
        value = searchQuery1,
        onValueChange = { searchQuery1 = it },
        colors = TextFieldDefaults.colors(
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            unfocusedContainerColor = Color.LightGray,
            focusedContainerColor = Color.LightGray,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        trailingIcon = {

            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )

                Text(text = "Math")

                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        },
        placeholder = {
            Text(
                text = "some text",
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        title = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Hi there", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.LightGray
                )
            }
        },
        actions = {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
            ) {
                Icon(imageVector = Icons.Rounded.Favorite, contentDescription = null)
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun HomePrev() {
    SageTheme {
//        HomeTopBar()
        HomeScreen()
    }
}
