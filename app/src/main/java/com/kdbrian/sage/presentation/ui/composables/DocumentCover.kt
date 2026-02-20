package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun DocumentCover(
    coverUrl: String,
    title: String,
    accentColor: Color,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(coverUrl.ifBlank { null })
            .crossfade(true)
            .build(),
        contentDescription = "Cover of $title",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(72.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(accentColor.copy(alpha = 0.3f)),
    )
}