package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.domain.model.DocumentModel

@Composable
fun DocumentList(
    documents: List<DocumentModel>,
    accentColors: Map<String, Color>,
    listState: LazyListState,
    onAccentColorResolved: (String, Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(documents, key = { it.id }) { doc ->
            DocumentCard(
                document = doc,
                accentColor = accentColors[doc.id],
                onAccentColorResolved = { color -> onAccentColorResolved(doc.id, color) },
            )
        }
    }
}
