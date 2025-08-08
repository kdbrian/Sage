package com.kdbrian.sage.domain.repo


//data class AnalyticEvent(
//
//)

interface AnalyticsAdapter {

    suspend fun logEvent(event: String, extras : Map<String, Any> = emptyMap())

}