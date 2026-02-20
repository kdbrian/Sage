package com.kdbrian.sage.presentation.nav

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
data class TopicDetailsRoute(val topicId: String) : Route()


@Serializable
data class ProfileRoute(val id: String) : Route()

@Serializable
data object Explore : Route()

@Serializable
data object Settings : Route()

@Serializable
data class DocumentDetailsRoute(val docId: String) : Route()


@Serializable
data object Search : Route()


@Serializable
data class SearchResultsRoute(val query: String) : Route()

