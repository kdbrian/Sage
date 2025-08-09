package com.kdbrian.sage.data.local.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class InAppActivity(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val name: String = "",
    val route: String? = "",
)
