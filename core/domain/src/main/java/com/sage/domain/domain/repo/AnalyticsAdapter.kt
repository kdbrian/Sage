package com.sage.domain.domain.repo


//data class AnalyticEvent(
//
//)

interface AnalyticsAdapter {

    suspend fun logEvent(event: String, extras : Map<String, Any> = emptyMap())

}