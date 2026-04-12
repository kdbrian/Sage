package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.kdbrian.sage.domain.model.DocumentModel
import kotlin.collections.getOrNull

@Composable
fun BookCardStack(
    modifier: Modifier = Modifier,
    books: LazyPagingItems<DocumentModel>,
) {
    val pagerState = rememberPagerState { books.itemCount }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(),
    ) {
        val currentBook = books[it]
        currentBook?.let {
            BookCard(
                book = currentBook,
            )
        }

    }
}