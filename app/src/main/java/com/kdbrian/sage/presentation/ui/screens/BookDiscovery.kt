package com.kdbrian.sage.presentation.ui.screens

import com.kdbrian.sage.domain.model.CategoryModel
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.domain.model.DocumentModel.Companion.sampleBooks
import com.kdbrian.sage.domain.model.DocumentModel.Companion.sampleCategories
import com.kdbrian.sage.presentation.ui.composables.BookBottomNavBar
import com.kdbrian.sage.presentation.ui.composables.CategoryChipRow
import kotlin.collections.filter


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.presentation.ui.composables.BookCardStack
import com.kdbrian.sage.presentation.ui.composables.SearchBar
import com.kdbrian.sage.presentation.ui.theme.SageTheme
import kotlinx.serialization.Serializable


@Serializable
data class BookDiscoveryState(
    val categories: List<CategoryModel> = emptyList(),
    val books: List<DocumentModel> = emptyList(),
    val selectedCategoryId: String = "",
    val searchQuery: String = "",
    val likedBookIds: Set<String> = emptySet(),
    val currentIndex: Int = 0
)


@Composable
fun BookDiscoveryScreen(
    initialState: BookDiscoveryState = BookDiscoveryState(
        categories = sampleCategories,
        books = sampleBooks,
        selectedCategoryId = sampleCategories.first().id
    )
) {
    var state by remember { mutableStateOf(initialState) }

    val filteredBooks = remember(state.selectedCategoryId, state.books, state.searchQuery) {
        state.books
            .filter { it.category == state.selectedCategoryId }
            .filter {
                state.searchQuery.isBlank() ||
                        it.title.contains(state.searchQuery, ignoreCase = true) ||
                        it.author.contains(state.searchQuery, ignoreCase = true)
            }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BookBottomNavBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            // Top Bar
            TopBarRow(
                onNotificationClick = { /* navigate */ },
                onProfileClick = { /* navigate */ }
            )

            Spacer(Modifier.height(16.dp))

            // Search
            SearchBar (
                query = state.searchQuery,
                onQueryChange = { state = state.copy(searchQuery = it) }
            )

            Spacer(Modifier.height(16.dp))

            // Category Chips
            CategoryChipRow(
                categories = state.categories,
                selectedId = state.selectedCategoryId,
                onSelect = { id ->
                    state = state.copy(selectedCategoryId = id, currentIndex = 0)
                }
            )

            Spacer(Modifier.height(24.dp))

            // Book Card Stack
            if (filteredBooks.isEmpty()) {
                EmptyState()
            } else {
                BookCardStack(
                    books = filteredBooks,
                    currentIndex = state.currentIndex,
                    likedBookIds = state.likedBookIds,
                    onSwipeLeft = {
                        if (state.currentIndex < filteredBooks.lastIndex)
                            state = state.copy(currentIndex = state.currentIndex + 1)
                    },
                    onSwipeRight = {
                        if (state.currentIndex < filteredBooks.lastIndex)
                            state = state.copy(currentIndex = state.currentIndex + 1)
                    },
                    onLike = { bookId ->
                        val newLiked = state.likedBookIds.toMutableSet().apply {
                            if (contains(bookId)) remove(bookId) else add(bookId)
                        }
                        state = state.copy(likedBookIds = newLiked)
                    },
                    onSkip = {
                        if (state.currentIndex < filteredBooks.lastIndex)
                            state = state.copy(currentIndex = state.currentIndex + 1)
                    }
                )
            }
        }
    }
}


@Composable
private fun TopBarRow(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onNotificationClick) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Color.White
            )
        }
        Spacer(Modifier.width(8.dp))
        // Profile avatar placeholder
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
                .clickable(onClick = onProfileClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF1A1A2E)
@Composable
fun BookDiscoveryPreview() {
    SageTheme {
        BookDiscoveryScreen()
    }
}