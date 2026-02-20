package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdbrian.sage.domain.model.ReferralInfo
import com.kdbrian.sage.presentation.ui.screens.CoinRewardChip

@Composable
fun ReferralBanner(
    referral: ReferralInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.Companion.horizontalGradient(
                    colors = listOf(Color(0xFFFF9A3C), Color(0xFFFFB347)),
                )
            )
            .clickable(onClick = onClick),
    ) {
        // Text content (left side)
        Column(
            modifier = Modifier.Companion
                .align(Alignment.Companion.CenterStart)
                .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                .fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Refer a friend",
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color.Companion.White,
            )

            // Coin chip
            CoinRewardChip(coins = referral.coinsPerReferral)
        }

        // Decorative panda emoji — real app would use an illustrated asset
        Row(
            modifier = Modifier.Companion
                .align(Alignment.Companion.CenterEnd)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy((-8).dp),
        ) {
            Text(text = "🐼", fontSize = 56.sp)
            Text(
                text = "🐼",
                fontSize = 44.sp,
                modifier = Modifier.Companion.offset(y = 8.dp),
            )
        }
    }
}