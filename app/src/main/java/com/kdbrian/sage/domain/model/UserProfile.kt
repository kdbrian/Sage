package com.kdbrian.sage.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val isFriend: Boolean = false,
)

val sampleProfile = UserProfile(
    id = "user_001",
    displayName = "Lucas Bennett",
    email = "lucasbennett@gmail.com",
    avatarUrl = "",
    followerCount = 1500,
    followingCount = 0,
    isFriend = false,
)

