package com.sage.library.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.More
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.sage.ui.composables.locals.LocalFontFamily
import com.sage.ui.theme.SageTheme

@Composable
fun DocumentListItem(
    modifier: Modifier = Modifier,
    documentTitle : String = LoremIpsum(2).values.joinToString(),
    documentSummary : String = LoremIpsum(12).values.joinToString(),
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ){

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ){

                Text(
                    text = documentTitle,
                    fontFamily = LocalFontFamily.current,
                    style = MaterialTheme.typography.titleLarge
                )

                BasicText(
                    text = documentSummary,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = LocalFontFamily.current,
                        fontWeight = FontWeight.W400,
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.padding(8.dp))

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.More,
                    contentDescription = null
                )
            }
        }

    }

}

@Preview
@Composable
private fun DocumentListItemPrev() {
    SageTheme {
        DocumentListItem()
    }
}