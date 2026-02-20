package com.kdbrian.sage.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReferralInfo(
    val coinsPerReferral: Int = 50,
    val referralCode: String = "",
)