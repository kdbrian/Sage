package com.kdbrian.sage.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val id: String,
    val label: String
)