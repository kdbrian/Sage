package com.kdbrian.sage.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kdbrian.sage.presentation.nav.Explore
import com.kdbrian.sage.presentation.nav.ProfileRoute
import com.kdbrian.sage.presentation.nav.Search
import com.kdbrian.sage.presentation.nav.Settings
import com.kdbrian.sage.presentation.ui.composables.BookBottomNavBar
import com.kdbrian.sage.presentation.ui.theme.SageTheme

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BookBottomNavBar()
        }
    ) { paddingValues ->

        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Explore
        ) {

            composable<Explore>{
                BookDiscoveryScreen()
            }
            composable<Search>{
                CategoriesScreen ()
            }
            composable<ProfileRoute>{
                ProfileScreen () { }
            }

            composable<Settings>{
                SettingsScreen()
            }


        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
    SageTheme {

        HomeScreen()
    }
}
