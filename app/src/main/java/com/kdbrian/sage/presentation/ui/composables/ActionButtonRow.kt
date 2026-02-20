package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.presentation.ui.theme.HeartRed

@Composable
fun ActionButtonRow(
    isLiked: Boolean,
    onSkip: () -> Unit,
    onShare: () -> Unit,
    onLike: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Skip
        ActionButton(
            onClick = onSkip,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Companion.White
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Skip",
                modifier = Modifier.Companion.size(24.dp)
            )
        }

        // Share / Info (primary, larger)
        ActionButton(
            onClick = onShare,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            size = 68.dp
        ) {
            Icon(
                Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.Companion.size(26.dp)
            )
        }

        // Like
        ActionButton(
            onClick = onLike,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = if (isLiked) HeartRed else Color.Companion.White
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                modifier = Modifier.Companion.size(24.dp)
            )
        }
    }
}