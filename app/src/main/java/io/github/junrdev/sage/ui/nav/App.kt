package io.github.junrdev.sage.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SCREENS.ONBOARDING.name) {


    }
}