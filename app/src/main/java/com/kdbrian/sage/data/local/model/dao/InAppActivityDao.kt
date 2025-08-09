package com.kdbrian.sage.data.local.model.dao

import com.kdbrian.sage.data.local.model.InAppActivity
import kotlinx.coroutines.flow.Flow

interface InAppActivityDao {

    fun logActivity(inAppActivity: InAppActivity)

    fun logs(): Flow<List<InAppActivity>>

    fun loadLogById(id: String): InAppActivity?

    fun clearLogs()
}
