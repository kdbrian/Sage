package com.kdbrian.sage.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.ui.composables.CategoryItem
import com.kdbrian.sage.ui.composables.RoundedInputField
import com.kdbrian.sage.ui.composables.RowButton
import com.kdbrian.sage.ui.theme.SageTheme
import com.kdbrian.sage.util.FileUtils
import com.kdbrian.sage.util.FileUtils.getFileName

@Composable
fun CreateScreen(modifier: Modifier = Modifier) {

    var docUri by remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }

    val context = LocalContext.current
    val iconAndText by remember {
        derivedStateOf {
            if (docUri != Uri.EMPTY && docUri != null) {
                context.getFileName(docUri!!) to Icons.Rounded.Upload
            } else {
                "Select Document." to Icons.Rounded.Add
            }
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Text(text = "Create", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = LoremIpsum(12).values.joinToString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                )

            }

            Spacer(Modifier.height(24.dp))

            RowButton(
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Row {
                        Icon(
                            imageVector = iconAndText.second,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = iconAndText.first ?: "Add Document",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
            )

            RoundedInputField(
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = "rename document"
            )

            RoundedInputField(
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = "Summary"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Topics", style = MaterialTheme.typography.titleLarge)


                    CategoryItem(text = "Add")
                }


            }

            LazyHorizontalGrid(
                rows = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
            ) {
                items(5) {
                    key(it) {
                        CategoryItem()
                    }
                }
            }

        }

    }
}

@Preview
@Composable
private fun CreateScreenPrev() {
    SageTheme {
        CreateScreen()
    }
}