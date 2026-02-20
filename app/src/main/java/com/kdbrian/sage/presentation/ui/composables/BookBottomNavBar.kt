package com.kdbrian.sage.presentation.ui.composables

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private const val TAG = "BookBottomNavBar"

@Composable
fun BookBottomNavBar(
    navHostController: NavHostController = rememberNavController()
) {

    val backStackEntryAsState = navHostController.currentBackStackEntryAsState()
    val currentRoute = backStackEntryAsState.value?.destination?.route

    LaunchedEffect(currentRoute) {
        Log.d(TAG, "BookBottomNavBar: Current $currentRoute")
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        BottomNavItem.entries.forEach { item ->
            val navigateSingleTop = {
                navHostController.navigate(item.route) {
                    popUpTo(navHostController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            val selected = currentRoute?.substringBefore("/") == item.route.toString()

            NavigationBarItem(
                selected = selected,
                onClick = navigateSingleTop,
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.White,
                )
            )
        }
    }
}