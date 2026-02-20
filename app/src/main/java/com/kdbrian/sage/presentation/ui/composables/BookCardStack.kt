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
    modifier: Modifier = Modifier,
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
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookCard(
            book = currentBook,
            isLiked = currentBook.id in likedBookIds,
            onSwipeLeft = onSwipeLeft,
            onSwipeRight = onSwipeRight
        )

    }
}