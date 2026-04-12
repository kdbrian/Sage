package com.kdbrian.sage.domain.repo

import com.kdbrian.sage.domain.model.InAppActivity
import kotlinx.coroutines.flow.Flow

interface AppActivityService {
    suspend fun logActivity(inAppActivity: InAppActivity)
    fun logs(): Flow<InAppActivity>
}