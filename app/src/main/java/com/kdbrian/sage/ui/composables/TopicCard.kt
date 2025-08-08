package com.kdbrian.sage.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.ui.theme.SageTheme

@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    text: AnnotatedString = buildAnnotatedString {
        append("Topic")
    },
    onClick: () -> Unit = {}
) {

    Surface(
        onClick = onClick,
        color = color,
        contentColor = contentColor,
        shadowElevation = 2.dp,
        modifier = modifier
            .requiredSize(100.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }


    }

}

@Preview
@Composable
private fun TopicCardPrev() {
    SageTheme {
        TopicCard()
    }
}