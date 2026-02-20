package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.auth.FirebaseAuth
import com.kdbrian.sage.presentation.nav.ExploreRoute
import com.kdbrian.sage.presentation.nav.ProfileRoute
import com.kdbrian.sage.presentation.nav.Route
import com.kdbrian.sage.presentation.nav.SearchRoute

enum class BottomNavItem(val icon: ImageVector, val label: String, val route: Route) {
    Explore(Icons.Default.Home, "Explore", ExploreRoute),
    Search(Icons.Default.Explore, "Search", SearchRoute),
    Profile(Icons.Default.Person, "Profile", ProfileRoute(FirebaseAuth.getInstance().currentUser?.uid ?: ""))
}

