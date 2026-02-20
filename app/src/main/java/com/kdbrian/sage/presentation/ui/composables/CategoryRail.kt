package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.domain.model.CategoryModel

@Composable
fun CategoryRail(
    categories: List<CategoryModel>,
    selectedId: String,
    listState: LazyListState,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        items(categories, key = { it.id }) { category ->
            CategoryRailItem(
                category = category,
                isSelected = category.id == selectedId,
                onClick = { onCategorySelected(category.id) },
            )
        }
    }
}
