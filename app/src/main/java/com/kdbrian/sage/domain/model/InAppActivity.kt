package com.kdbrian.sage.domain.model

import android.service.notification.Condition.newId
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.uuid.Uuid

@Entity
class InAppActivity internal constructor(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val route: String? = "",
    val timestamp: Long = System.currentTimeMillis(),
) {
    class Builder {
        var id: String = UUID.randomUUID().toString()
        var name: String = ""
        var route: String? = ""
        var timestamp: Long = System.currentTimeMillis()


        fun id(newId: String) = apply { id = newId }
        fun name(newName: String) = apply { name = newName }
        fun route(r: String) = apply { route = r }
        fun timeStamp(millis: Long) = apply { timestamp = millis }

        fun build() = InAppActivity(
            id, name, route, timestamp
        )

    }
}