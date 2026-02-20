package com.kdbrian.sage.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StatCard(
    val id: String,
    val emoji: String,           // Unicode emoji used as icon
    val value: String,           // "51", "1", "Barefoot", "30"
    val label: String,           // "Balance", "Level", "Current League", "Total XP"
    val hasRecord: Boolean = false,
    val accentHex: String = "FFDD55",  // background tint of the emoji bubble
)

val sampleStatCards = listOf(
    StatCard(
        id = "balance",
        emoji = "⭐",
        value = "51",
        label = "Balance",
        accentHex = "FFD700",
    ),
    StatCard(
        id = "level",
        emoji = "🏆",
        value = "1",
        label = "Level",
        hasRecord = true,
        accentHex = "FFB347",
    ),
    StatCard(
        id = "league",
        emoji = "👣",
        value = "Barefoot",
        label = "Current League",
        accentHex = "FFB6A3",
    ),
    StatCard(
        id = "xp",
        emoji = "⚡",
        value = "30",
        label = "Total XP",
        accentHex = "FFE066",
    ),
)
