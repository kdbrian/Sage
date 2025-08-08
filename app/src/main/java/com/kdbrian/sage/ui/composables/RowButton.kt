package com.kdbrian.sage.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.ui.theme.SageTheme

@Composable
fun RowButton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = RoundedCornerShape(12.dp),
    leadingIcon: @Composable () -> Unit = {
        Text(
            text = LoremIpsum(2).values.joinToString(),
            style = MaterialTheme.typography.titleMedium
        )
    },
    trailingIcon: @Composable () -> Unit = {
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = null)
        }
    },
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            leadingIcon()
            trailingIcon()

        }

    }
}

@Preview
@Composable
private fun RowButtonPrev() {
    SageTheme {
        RowButton()
    }
}