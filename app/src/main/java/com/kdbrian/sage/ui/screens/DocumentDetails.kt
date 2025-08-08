package com.kdbrian.sage.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kdbrian.sage.App
import com.kdbrian.sage.R
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetails(
    navHostController: NavHostController = rememberNavController(),
    documentId: String = UUID.randomUUID().toString()
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


    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 3.dp
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = LoremIpsum(2).values.joinToString())
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.popBackStack()
                        }) {
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
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {


            Text(
                text = LoremIpsum(3).values.joinToString(),
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.padding(12.dp))

            Text(
                text = LoremIpsum(24).values.joinToString(),
                style = MaterialTheme.typography.bodyLarge
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
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
                        text = LoremIpsum(3).values.joinToString(),
                        style = MaterialTheme.typography.bodyMedium
                    )


                    Text(
                        text = LoremIpsum(3).values.joinToString(),
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(16.dp),
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
                        text = LoremIpsum(18).values.joinToString(),
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
    App {
        DocumentDetails()
    }
}