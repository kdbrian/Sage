package com.kdbrian.sage.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kdbrian.sage.domain.model.StatCard
import com.kdbrian.sage.domain.model.UserProfile
import com.kdbrian.sage.domain.model.sampleProfile
import com.kdbrian.sage.domain.model.sampleStatCards
import com.kdbrian.sage.presentation.ui.theme.*
import com.kdbrian.sage.util.toDisplayString


data class ProfileUiState(
    val profile: UserProfile = UserProfile(),
    val statCards: List<StatCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)


@Composable
fun ProfileScreen(
    uiState: ProfileUiState = ProfileUiState(),
    onBack: () -> Unit = {},
    onSettings: () -> Unit = {},
    onFriendToggled: () -> Unit = {},
    onStatCardClicked: (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            // ── Teal Header ──────────────────────────────────────────────────
            ProfileHeader(
                profile = uiState.profile,
                onBack = onBack,
                onSettings = onSettings,
                onFriendToggled = onFriendToggled,
            )

            Spacer(Modifier.height(16.dp))

            // ── Stat Grid ───────────────────────────────────────────────────
            StatCardGrid(
                cards = uiState.statCards,
                onCardClicked = onStatCardClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}


@Composable
fun ProfileHeader(
    profile: UserProfile,
    onBack: () -> Unit = {},
    onSettings: () -> Unit = {},
    onFriendToggled: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Extra bottom padding so the avatar can overlap below
            .padding(bottom = 36.dp),
    ) {
        // ── Teal background slab ─────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    color = TealHeader,
                    shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp),
                ),
        ) {
            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(40.dp)
                    .background(CardWhite, RoundedCornerShape(12.dp)),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary,
                )
            }

            // Settings button
            IconButton(
                onClick = onSettings,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(40.dp)
                    .background(CardWhite, RoundedCornerShape(12.dp)),
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextPrimary,
                )
            }
        }

        // ── Avatar — overlaps header / body boundary ──────────────────────
        ProfileAvatar(
            avatarUrl = profile.avatarUrl,
            displayName = profile.displayName,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp)
                .offset(y = 8.dp),
        )

        // ── Friend button ─────────────────────────────────────────────────
        FriendButton(
            isFriend = profile.isFriend,
            onClick = onFriendToggled,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 0.dp),
        )
    }

    // ── Name / email + follower stats ─────────────────────────────────────
    ProfileIdentity(profile = profile)
}


@Composable
fun ProfileAvatar(
    avatarUrl: String,
    displayName: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(84.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .border(3.dp, CardWhite, CircleShape)
            .background(Color(0xFFDDDDDD), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarUrl.ifBlank { null })
                .crossfade(true)
                .build(),
            contentDescription = "Avatar of $displayName",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}


@Composable
fun FriendButton(
    isFriend: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isFriend) TealHeader else RecordBadge,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "FriendBorderColor",
    )
    val textColor by animateColorAsState(
        targetValue = if (isFriend) TealHeader else RecordBadge,
        label = "FriendTextColor",
    )

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
            containerColor = CardWhite,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        modifier = modifier.height(36.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = if (isFriend) "Friends" else "+ Friends",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun ProfileIdentity(profile: UserProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Name + email
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = profile.displayName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary,
            )
            Text(
                text = profile.email,
                fontSize = 12.sp,
                color = TextSecondary,
            )
        }

        // Follower / Following
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            SocialStat(value = profile.followerCount.toDisplayString(), label = "Followers")
            SocialStat(value = profile.followingCount.toDisplayString(), label = "Following")
        }
    }
}

@Composable
private fun SocialStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextPrimary,
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary,
        )
    }
}


@Composable
fun StatCardGrid(
    cards: List<StatCard>,
    onCardClicked: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // Chunk into rows of 2
    val rows = cards.chunked(2)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEach { card ->
                    StatCardItem(
                        card = card,
                        onClick = { onCardClicked(card.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
                // Fill empty slot if odd number
                if (row.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun StatCardItem(
    card: StatCard,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "CardScale",
    )

    val accentColor = remember(card.accentHex) {
        runCatching { Color(android.graphics.Color.parseColor("#${card.accentHex}")) }
            .getOrDefault(Color(0xFFFFDD55))
    }

    Box(
        modifier = modifier
            .aspectRatio(1.1f)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Emoji icon bubble
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = card.emoji, fontSize = 20.sp)
            }

            Column {
                Text(
                    text = card.value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Text(
                    text = card.label,
                    fontSize = 12.sp,
                    color = TextSecondary,
                )
            }
        }

        // "Record" badge
        if (card.hasRecord) {
            RecordBadge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
            )
        }
    }
}


@Composable
fun RecordBadge(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(50),
        color = RecordBadge,
        modifier = modifier,
    ) {
        Text(
            text = "Record",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF4F4F4, widthDp = 360, heightDp = 700)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            uiState = ProfileUiState(
                profile = sampleProfile,
                statCards = sampleStatCards,
            ),
            onBack = {},
            onSettings = {},
            onFriendToggled = {},
            onStatCardClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatCardGridPreview() {
    MaterialTheme {
        StatCardGrid(
            cards = sampleStatCards,
            onCardClicked = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecordBadgePreview() {
    MaterialTheme { RecordBadge() }
}