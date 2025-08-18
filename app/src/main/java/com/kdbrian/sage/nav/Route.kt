package com.kdbrian.sage.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Route


@Serializable
data object GetStartedRoute : Route()


@Serializable
data object HomeScreenRoute : Route()


@Serializable
data object CreateScreenRoute : Route()


@Serializable
data class TopicDetailsRoute(val topicId : String) : Route()


@Serializable
data object ProfileRoute : Route()


@Serializable
data class DocumentDetailsRoute(val docId : String): Route()


@Serializable
data class SearchResultsRoute(val query : String): Route()

