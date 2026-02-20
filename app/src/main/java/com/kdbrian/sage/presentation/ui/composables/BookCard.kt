package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.sage.domain.model.DocumentModel

@Composable
fun BookCard(
    book: DocumentModel,
    isLiked: Boolean,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardOffset"
    )

    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .graphicsLayer { translationX = animatedOffset }
            .pointerInput(book.id) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > 150f -> {
                                offsetX = 0f; onSwipeRight()
                            }

                            offsetX < -150f -> {
                                offsetX = 0f; onSwipeLeft()
                            }

                            else -> offsetX = 0f
                        }
                    }
                ) { _, dragAmount ->
                    offsetX += dragAmount
                }
            }
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.Companion
                .fillMaxWidth()
                .aspectRatio(0.72f)
                .shadow(16.dp, androidx.compose.foundation.shape.RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.Companion.Transparent)
        ) {
            Box(modifier = Modifier.Companion.fillMaxSize()) {
                // Cover image
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    contentScale = ContentScale.Companion.Crop,
                    modifier = Modifier.Companion.fillMaxSize()
                )

                // Gradient overlay at bottom
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f)
                        .align(Alignment.Companion.BottomCenter)
                        .background(
                            Brush.Companion.verticalGradient(
                                colors = listOf(Color.Companion.Transparent, Color(0xDD000000))
                            )
                        )
                )

                // Title + author
                Column(
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = book.title,
                        color = Color.Companion.White,
                        fontWeight = FontWeight.Companion.Bold,
                        fontSize = 22.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Companion.Ellipsis
                    )
                    Spacer(Modifier.Companion.height(2.dp))
                    Text(
                        text = book.author,
                        color = Color.Companion.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}