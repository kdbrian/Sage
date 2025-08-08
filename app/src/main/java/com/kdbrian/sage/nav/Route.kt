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
data object TopicDetailsRoute : Route()


@Serializable
data object ProfileRoute : Route()


@Serializable
data class DocumentDetailsRoute(val docId : String): Route()

