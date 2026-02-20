package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdbrian.sage.domain.model.CategoryModel

@Composable
fun CategoryChip(
    categoryModel: CategoryModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected) MaterialTheme.colorScheme.primary else Color.Companion.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Companion.White
    val border = if (isSelected) null else BorderStroke(
        1.dp,
        Color.Companion.Gray.copy(alpha = 0.4f)
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = background,
        border = border,
        modifier = Modifier.Companion.height(36.dp)
    ) {
        Box(
            contentAlignment = Alignment.Companion.Center,
            modifier = Modifier.Companion.padding(horizontal = 18.dp)
        ) {
            Text(
                text = categoryModel.label,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Companion.Bold else FontWeight.Companion.Normal,
                fontSize = 14.sp
            )
        }
    }
}