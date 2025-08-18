package com.kdbrian.sage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdbrian.sage.ui.theme.SageTheme

@Composable
fun GetStarted(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit = {},
    onSkip: () -> Unit = {},
) {

    Surface(
        modifier = modifier,
        contentColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f),
                            Color(0xFF153131),
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(Modifier.weight(1f))

                TextButton(
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    ),
                    onClick = onSkip
                ) {
                    Text("Skip")
                }

                Text(
                    text = buildAnnotatedString {
                        append("Get")
                        append("\n")
                        append("Started")
                    },
                    style = MaterialTheme.typography.displayLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                )

                Spacer(
                    Modifier
                        .padding(24.dp)
                        .background(color = Color.Red)
                )

                Column {
                    RowAction(text = "Save documents")
                    RowAction(text = "Sync saved documents & favourites.")
                    RowAction(text = "Access a community of like minded people.")
                    RowAction(text = "Receive recommendations.")

                    RowAction(text = "Backup all your activity.")
                }

                Spacer(Modifier.padding(16.dp))

                TextButton(onClick = onGetStarted, modifier = Modifier) {
                    Text(text = "Join Today")
                }


                Spacer(Modifier.weight(1f))

                Text(
                    text = LoremIpsum(24).values.joinToString(),
                    modifier = Modifier

                        .widthIn(max = 230.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.Center,
                        color = Color.LightGray,
                        fontSize = 10.sp
                    )
                )

                Spacer(Modifier.weight(1f))


            }


        }
    }


}

@Composable
private fun RowAction(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = null,
            tint = Color.Green
        )
    },
    text: String = LoremIpsum(2).values.joinToString(),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        icon()

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Light
            )
        )
    }
}

@Preview
@Composable
private fun GetStartedPrev() {
    SageTheme {
        GetStarted()
    }
}