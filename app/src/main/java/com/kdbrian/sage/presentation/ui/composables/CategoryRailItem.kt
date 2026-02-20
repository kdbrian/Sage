package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdbrian.sage.domain.model.CategoryModel

@Composable
fun CategoryRailItem(
    category: CategoryModel,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val defaultColor = MaterialTheme.colorScheme.onSurfaceVariant

    val textColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor else defaultColor,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "CategoryTextColor",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .drawBehind {
                if (isSelected) {
                    // Wavy left-side highlight — simple arc approximation
                    val paint = androidx.compose.ui.graphics.Paint().apply {
                        this.color = selectedColor.copy(alpha = 0.12f)
                    }
                    drawRect(
                        color = selectedColor.copy(alpha = 0.12f),
                        topLeft = Offset(0f, 0f),
                        size = size,
                    )
                    // Right edge indicator
                    drawRect(
                        color = selectedColor,
                        topLeft = Offset(size.width - 4f, size.height * 0.2f),
                        size = androidx.compose.ui.geometry.Size(4f, size.height * 0.6f),
                    )
                }
            },
    ) {
        Text(
            text = category.label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.graphicsLayer { rotationZ = -90f },
            maxLines = 1,
        )
    }
}