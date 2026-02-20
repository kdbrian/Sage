package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.domain.model.DocumentModel
import kotlin.collections.getOrNull

@Composable
fun BookCardStack(
    books: List<DocumentModel>,
    currentIndex: Int,
    likedBookIds: Set<String>,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onLike: (String) -> Unit,
    onSkip: () -> Unit
) {
    val currentBook = books.getOrNull(currentIndex) ?: return

    Column(
        modifier = Modifier.Companion.fillMaxWidth(),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        BookCard(
            book = currentBook,
            isLiked = currentBook.id in likedBookIds,
            onSwipeLeft = onSwipeLeft,
            onSwipeRight = onSwipeRight
        )

        Spacer(Modifier.Companion.height(24.dp))

        // Action Buttons
        ActionButtonRow(
            isLiked = currentBook.id in likedBookIds,
            onSkip = onSkip,
            onShare = { /* share */ },
            onLike = { onLike(currentBook.id) }
        )
    }
}