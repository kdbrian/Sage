package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(val icon: ImageVector, val label: String) {
    Home(Icons.Default.Home, "Home"),
    Discover(Icons.Default.Explore, "Discover"),
    Profile(Icons.Default.Person, "Profile")
}

