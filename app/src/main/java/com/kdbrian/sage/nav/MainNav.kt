package com.kdbrian.sage.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNav(
    navHostController: NavHostController = rememberNavController()
) {

    NavHost(navController = navHostController, startDestination = "home") {




    }


}