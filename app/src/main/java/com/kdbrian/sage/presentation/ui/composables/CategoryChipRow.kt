package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.domain.model.CategoryModel

@Composable
fun CategoryChipRow(
    categories: List<CategoryModel>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            CategoryChip(
                categoryModel = category,
                isSelected = category.id == selectedId,
                onClick = { onSelect(category.id) }
            )
        }
    }
}