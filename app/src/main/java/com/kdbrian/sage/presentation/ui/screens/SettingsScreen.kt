package com.kdbrian.sage.presentation.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdbrian.sage.domain.model.ReferralInfo
import com.kdbrian.sage.presentation.ui.composables.PremiumStatusCard
import com.kdbrian.sage.presentation.ui.composables.ReferralBanner
import com.kdbrian.sage.presentation.ui.theme.CardBg
import com.kdbrian.sage.presentation.ui.theme.ChevronTint
import com.kdbrian.sage.presentation.ui.theme.DividerColor
import com.kdbrian.sage.presentation.ui.theme.IconTint
import com.kdbrian.sage.presentation.ui.theme.PageBg
import com.kdbrian.sage.presentation.ui.theme.TextPrimary
import kotlinx.serialization.Serializable


// ─────────────────────────────────────────────────────────────────────────────
// Data Models
// ─────────────────────────────────────────────────────────────────────────────

/** Identifies each settings destination so the caller can route correctly. */
@Serializable
enum class SettingsDestination {
    EMAIL,
    USERNAME,
    STEP_DATA_SOURCE,
    LANGUAGE,
    PRIVACY,
    PREMIUM_STATUS,
    REFER_A_FRIEND,
}

@Serializable
data class SettingsMenuItem(
    val destination: SettingsDestination,
    val label: String,
    /** Material icon; stored as name only for serialization; resolved at runtime. */
    val iconName: String = "",
)

@Serializable
data class PremiumStatusInfo(
    val isActive: Boolean = false,
    /** e.g. "Pro", "Gold" when active */
    val planName: String = "",
)


data class SettingsUiState(
    val menuItems: List<SettingsMenuItem> = defaultMenuItems,
    val premium: PremiumStatusInfo = PremiumStatusInfo(),
    val referral: ReferralInfo = ReferralInfo(),
    val isLoading: Boolean = false,
    val error: String? = null,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState = SettingsUiState(),
    onBack: () -> Unit = {},
    onMenuItemClicked: (SettingsDestination) -> Unit = {},
    onPremiumClicked: () -> Unit = {},
    onReferralClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .shadow(2.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardBg),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = IconTint,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
        ) {

            Spacer(Modifier.height(16.dp))

            // ── Main menu card ───────────────────────────────────────────────────
            SettingsMenuCard(
                items = uiState.menuItems,
                onItemClicked = onMenuItemClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(16.dp))

            // ── Premium status card ──────────────────────────────────────────────
            PremiumStatusCard(
                premium = uiState.premium,
                onClick = onPremiumClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(16.dp))

            // ── Referral banner ──────────────────────────────────────────────────
            ReferralBanner(
                referral = uiState.referral,
                onClick = onReferralClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}


@Composable
fun SettingsMenuCard(
    items: List<SettingsMenuItem>,
    onItemClicked: (SettingsDestination) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = CardBg,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            items.forEachIndexed { index, item ->
                SettingsMenuRow(
                    item = item,
                    onClick = { onItemClicked(item.destination) },
                )
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        color = DividerColor,
                        thickness = 0.8.dp,
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsMenuRow(
    item: SettingsMenuItem,
    onClick: () -> Unit = {},
) {
    val ripple = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "RowScale",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(interactionSource = ripple, indication = null, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon in a soft rounded box outline
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.destination.toIcon(),
                contentDescription = item.label,
                tint = IconTint,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Text(
            text = item.label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
        )

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = ChevronTint,
            modifier = Modifier.size(20.dp),
        )
    }
}


fun SettingsDestination.toIcon(): ImageVector = when (this) {
    SettingsDestination.EMAIL -> Icons.Outlined.Email
    SettingsDestination.USERNAME -> Icons.Outlined.Person
    SettingsDestination.STEP_DATA_SOURCE -> Icons.Outlined.Directions
    SettingsDestination.LANGUAGE -> Icons.Outlined.Translate
    SettingsDestination.PRIVACY -> Icons.Outlined.Shield
    SettingsDestination.PREMIUM_STATUS -> Icons.Outlined.Diamond
    SettingsDestination.REFER_A_FRIEND -> Icons.Outlined.CardGiftcard
}


private val defaultMenuItems = listOf(
    SettingsMenuItem(destination = SettingsDestination.EMAIL, label = "Email"),
    SettingsMenuItem(destination = SettingsDestination.USERNAME, label = "Username"),
    SettingsMenuItem(
        destination = SettingsDestination.STEP_DATA_SOURCE,
        label = "Step data source"
    ),
    SettingsMenuItem(destination = SettingsDestination.LANGUAGE, label = "Language"),
    SettingsMenuItem(destination = SettingsDestination.PRIVACY, label = "Privacy"),
)


@Preview(showBackground = true, backgroundColor = 0xFFF2F2F7, widthDp = 375, heightDp = 720)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                menuItems = defaultMenuItems,
                premium = PremiumStatusInfo(isActive = false),
                referral = ReferralInfo(coinsPerReferral = 50),
            ),
            onBack = {},
            onMenuItemClicked = {},
            onPremiumClicked = {},
            onReferralClicked = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
fun ReferralBannerPreview() {
    MaterialTheme {
        ReferralBanner(
            referral = ReferralInfo(coinsPerReferral = 50),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
fun PremiumStatusCardPreview() {
    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            PremiumStatusCard(premium = PremiumStatusInfo(isActive = false), onClick = {})
            PremiumStatusCard(
                premium = PremiumStatusInfo(isActive = true, planName = "Pro"),
                onClick = {})
        }
    }
}