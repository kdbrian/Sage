package com.sage.data.remote.impl

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.sage.domain.domain.repo.AnalyticsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnalyticsAdapterImpl(
    private val analytics: FirebaseAnalytics
) : AnalyticsAdapter {
    override suspend fun logEvent(event: String, extras: Map<String, Any>) =
        withContext(Dispatchers.Default) {
            val bundle = bundleOf(*extras.toList().toTypedArray())
            analytics.logEvent(event, bundle)
        }
}