package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdbrian.sage.presentation.ui.screens.PremiumStatusInfo
import com.kdbrian.sage.presentation.ui.theme.CardBg
import com.kdbrian.sage.presentation.ui.theme.DiamondBlue
import com.kdbrian.sage.presentation.ui.theme.InactiveOrange
import com.kdbrian.sage.presentation.ui.theme.TextPrimary

@Composable
fun PremiumStatusCard(
    premium: PremiumStatusInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    val statusText = if (premium.isActive) premium.planName.ifBlank { "Active" } else "Inactive"
    val statusColor by animateColorAsState(
        targetValue = if (premium.isActive) Color(0xFF34C759) else InactiveOrange,
        label = "PremiumStatusColor",
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = CardBg,
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
        ) {
            // Diamond icon
            Icon(
                imageVector = Icons.Outlined.Diamond,
                contentDescription = "Premium",
                tint = DiamondBlue,
                modifier = Modifier.Companion.size(24.dp),
            )

            Spacer(Modifier.Companion.width(14.dp))

            Text(
                text = "Premium Status",
                fontSize = 15.sp,
                fontWeight = FontWeight.Companion.Normal,
                color = TextPrimary,
                modifier = Modifier.Companion.weight(1f),
            )

            // Status label
            Text(
                text = statusText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                color = statusColor,
            )

            Spacer(Modifier.Companion.width(4.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.Companion.size(20.dp),
            )
        }
    }
}