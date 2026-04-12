package com.kdbrian.sage.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.kdbrian.sage.presentation.nav.ExploreRoute
import com.kdbrian.sage.presentation.nav.ProfileRoute
import com.kdbrian.sage.presentation.nav.SearchRoute
import com.kdbrian.sage.presentation.nav.SettingsRoute
import com.kdbrian.sage.presentation.ui.composables.BookBottomNavBar
import com.kdbrian.sage.presentation.ui.theme.SageTheme
import com.kdbrian.sage.presentation.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = koinViewModel()
) {

    val lazyBooks = mainViewModel.books.collectAsLazyPagingItems()


    Scaffold(
        bottomBar = {
            BookBottomNavBar(
                navHostController = navController
            )
        }
    ) { _ ->

        NavHost(
            navController = navController,
            startDestination = ExploreRoute
        ) {

            composable<ExploreRoute> {
                BookDiscoveryScreen(
                    books = lazyBooks
                )
            }
            composable<SearchRoute> {
                CategoriesScreen()
            }
            composable<ProfileRoute> {
                ProfileScreen(
                    onSettings = {
                        navController.navigate(SettingsRoute)
                    }
                )
            }

            composable<SettingsRoute> {
                SettingsScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
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
