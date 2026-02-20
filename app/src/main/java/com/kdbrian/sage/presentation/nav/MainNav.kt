package com.kdbrian.sage.presentation.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kdbrian.sage.presentation.ui.screens.BookDiscoveryScreen

@Composable
fun MainNav() {

    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = HomeScreenRoute
    ) {

        composable<HomeScreenRoute> {
            BookDiscoveryScreen()
        }


    }


}