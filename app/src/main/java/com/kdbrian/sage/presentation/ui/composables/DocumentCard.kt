package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.util.extractAccentColor
import kotlinx.coroutines.launch

@Composable
fun DocumentCard(
    document: DocumentModel,
    accentColor: Color?,
    onAccentColorResolved: (Color) -> Unit,
    onClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Resolve accent color once when the card first appears
    LaunchedEffect(document.coverUrl) {
        if (accentColor == null && document.coverUrl.isNotBlank()) {
            scope.launch {
                val color = extractAccentColor(context, document.coverUrl)
                onAccentColorResolved(color)
            }
        }
    }

    val resolvedAccent = accentColor ?: MaterialTheme.colorScheme.surfaceVariant
    val cardBackground by animateColorAsState(
        targetValue = resolvedAccent.copy(alpha = 0.18f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "CardBackground",
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Cover Image
            DocumentCover(
                coverUrl = document.coverUrl,
                title = document.title,
                accentColor = resolvedAccent,
            )

            Spacer(Modifier.width(14.dp))

            // Meta
            DocumentMeta(
                document = document,
                accentColor = resolvedAccent,
            )
        }
    }
}
