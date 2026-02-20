package com.kdbrian.sage.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdbrian.sage.presentation.ui.theme.CoinYellow

@Composable
fun CoinRewardChip(coins: Int) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Companion.White.copy(alpha = 0.25f),
    ) {
        Row(
            modifier = Modifier.Companion.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // Coin icon
            Box(
                modifier = Modifier.Companion
                    .size(20.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                    .background(CoinYellow),
                contentAlignment = Alignment.Companion.Center,
            ) {
                Text(text = "🪙", fontSize = 12.sp)
            }

            Text(
                text = "$coins /referral",
                fontSize = 12.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                color = Color.Companion.White,
            )
        }
    }
}