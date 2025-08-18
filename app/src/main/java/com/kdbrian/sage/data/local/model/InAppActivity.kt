package com.kdbrian.sage.data.local.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

class InAppActivity(
    @PrimaryKey
    val _id: org.mongodb.kbson.ObjectId = org.mongodb.kbson.ObjectId(),
    val timestamp: Long = System.currentTimeMillis(),
    val name: String = "",
    val route: String? = "",
) : RealmObject
