package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RatingChip(rating: Float, accentColor: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = accentColor.copy(alpha = 0.25f),
        modifier = Modifier.wrapContentSize(),
    ) {
        Text(
            text = "★ ${"%.1f".format(rating)}",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = accentColor,
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}