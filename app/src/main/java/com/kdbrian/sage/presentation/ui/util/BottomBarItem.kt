package com.kdbrian.sage.presentation.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.kdbrian.sage.presentation.nav.CreateScreenRoute
import com.kdbrian.sage.presentation.nav.HomeScreenRoute
import com.kdbrian.sage.presentation.nav.Route

data class BottomBarItem(
    val label: String,
    val icon: ImageVector,
    val route: Route,
) {
    companion object {
        val twoScreenItems = listOf(
            BottomBarItem(
                label = "Home",
                icon = Icons.Rounded.Home,
                route = HomeScreenRoute
            ),
            BottomBarItem(
                label = "Create",
                icon = Icons.Rounded.Add,
                route = CreateScreenRoute
            )
        )
    }
}
