package com.kdbrian.sage.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.App

@Composable
fun ListItemPreview(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.padding(6.dp))

            Column(
                horizontalAlignment = Alignment.Start,

                ) {

                Text(
                    text = LoremIpsum(3).values.joinToString(),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = buildAnnotatedString {
                        append("Upto ")
                        withStyle(
                            SpanStyle(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("300 ")
                        }
                        append("documents.")
                    },
                    style = MaterialTheme.typography.labelMedium
                )
            }

        }


    }

}

@Preview
@Composable
private fun TopicItemPreviewPrev() {
    App {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ListItemPreview()
        }
    }
}