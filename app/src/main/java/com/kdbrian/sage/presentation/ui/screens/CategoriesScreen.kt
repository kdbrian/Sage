package com.kdbrian.sage.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kdbrian.sage.domain.model.CategoryModel
import com.kdbrian.sage.domain.model.DocumentModel
import com.kdbrian.sage.domain.model.DocumentModel.Companion.sampleBooks
import com.kdbrian.sage.domain.model.DocumentModel.Companion.sampleCategories
import com.kdbrian.sage.presentation.ui.composables.CategoryRail
import com.kdbrian.sage.presentation.ui.composables.DocumentList
import com.kdbrian.sage.presentation.ui.theme.SageTheme

data class CategoriesUiState(
    val categories: List<CategoryModel> = emptyList(),
    val selectedCategoryId: String = "",
    val documents: List<DocumentModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val accentColors: Map<String, Color> = emptyMap(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    uiState: CategoriesUiState = CategoriesUiState(
        categories = sampleCategories,
        documents = sampleBooks,
    ),
    onBack: () -> Unit = {},
    onCategorySelected: (String) -> Unit = {},
    onAccentColorResolved: (String, Color) -> Unit = { _, _ -> },
) {
    val categoryListState = rememberLazyListState()
    val documentListState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = "Back",
                    )
                }
            },
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // ── Left: Category Rail ──────────────────────
            CategoryRail(
                categories = uiState.categories,
                selectedId = uiState.selectedCategoryId,
                listState = categoryListState,
                onCategorySelected = onCategorySelected,
                modifier = Modifier
                    .width(56.dp)
                    .fillMaxHeight(),
            )

            // ── Right: Document List ─────────────────────
            DocumentList(
                documents = uiState.documents,
                accentColors = uiState.accentColors,
                listState = documentListState,
                onAccentColorResolved = onAccentColorResolved,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
    }
}


@Preview
@Composable
private fun CategoriesScreenPrev() {
    SageTheme {
        CategoriesScreen()
    }
}