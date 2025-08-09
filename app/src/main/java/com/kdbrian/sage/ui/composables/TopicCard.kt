package com.kdbrian.sage.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.ui.theme.SageTheme

@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
//    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
//    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    text: AnnotatedString = buildAnnotatedString {
        append(LoremIpsum(2).values.joinToString())
    },
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = modifier.padding(4.dp),
        shadowElevation = 2.dp,
        onClick = onClick,
        shape = RoundedCornerShape(50)
    ) {
        Box(
            modifier = modifier
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                textAlign = TextAlign.Center
            )

        }
    }

}

@Preview(
    showBackground = true,
)
@Composable
private fun TopicCardPrev() {
    SageTheme {
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth()
                .requiredHeightIn(max = 130.dp)
        ) {
            items(10){index->
                key(index){
                    TopicCard()
                }
            }
        }
    }
}