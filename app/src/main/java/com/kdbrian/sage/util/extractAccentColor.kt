package com.kdbrian.sage.util

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil3.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Loads [coverUrl], extracts a dominant / vibrant swatch using
 * AndroidX Palette and returns a Compose [Color].
 *
 * Falls back to [fallback] if anything goes wrong.
 */
suspend fun extractAccentColor(
    context: Context,
    coverUrl: String,
    fallback: Color = Color(0xFF90CAF9),
): Color = withContext(Dispatchers.IO) {
    try {
        val url = URL(coverUrl)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            doInput = true
            connectTimeout = 10_000
            readTimeout = 10_000
            connect()
        }

        val bitmap: Bitmap = connection.inputStream.use { input ->
            BitmapFactory.decodeStream(input)
        } ?: return@withContext fallback

        val palette = Palette.from(bitmap)
            .clearFilters()
            .generate()

        val colorInt = palette.vibrantSwatch?.rgb
            ?: palette.dominantSwatch?.rgb
            ?: palette.mutedSwatch?.rgb
            ?: fallback.toArgb()

        Color(colorInt)
    } catch (e: Exception) {
        fallback
    }
}
