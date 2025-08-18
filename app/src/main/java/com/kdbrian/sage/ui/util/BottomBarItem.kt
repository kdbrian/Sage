package com.kdbrian.sage.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.kdbrian.sage.nav.CreateScreenRoute
import com.kdbrian.sage.nav.HomeScreenRoute
import com.kdbrian.sage.nav.Route

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
