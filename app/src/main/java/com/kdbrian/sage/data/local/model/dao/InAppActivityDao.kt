package com.kdbrian.sage.data.local.model.dao

import com.kdbrian.sage.data.local.model.InAppActivity
import kotlinx.coroutines.flow.Flow

interface InAppActivityDao {

    suspend fun logActivity(inAppActivity: InAppActivity)

    suspend fun logs(): Flow<InAppActivity>

    suspend fun loadLogById(id: String): InAppActivity?

    suspend fun clearLogs()
}
