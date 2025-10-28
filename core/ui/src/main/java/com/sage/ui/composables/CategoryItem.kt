package com.sage.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sage.ui.theme.SageTheme


@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    text: String = LoremIpsum(2).values.joinToString(),
    onClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(24.dp)
) {

    Surface(
        modifier = modifier
            .requiredWidthIn(min = 100.dp, max = 150.dp)
            .requiredHeightIn(min = 30.dp, max = 35.dp),
        shape = shape,
        color = color,
        contentColor = contentColor,
        onClick = onClick,
        shadowElevation = 2.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()

            BasicText(
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight(300),
                    textAlign = TextAlign.Center
                ),
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 14.sp,
                    maxFontSize = 16.sp,
                    stepSize = 1.sp
                ),
                text = text,
                modifier = Modifier.padding(4.dp)
            )
        }

    }
}

@Preview
@Composable
private fun CategoryItemPrev() {
    SageTheme {
        CategoryItem()
    }
}