package com.kdbrian.sage.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kdbrian.sage.nav.CreateScreenRoute
import com.kdbrian.sage.nav.HomeScreenRoute
import com.kdbrian.sage.ui.state.HomeScreenViewModel
import com.kdbrian.sage.ui.util.BottomBarItem
import com.sage.ui.theme.SageTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    onSearch: (String) -> Unit = {},
    onOpenProfile: () -> Unit,
    onTopicExpand: (String) -> Unit,
    onOpenFYP: () -> Unit = {}
) {

    val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()
    val uiState by homeScreenViewModel.uiState.collectAsState()
    val backStackEntryAsState = navHostController.currentBackStackEntryAsState()

    Scaffold(
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navHostController,
            startDestination = HomeScreenRoute
        ) {
            composable<HomeScreenRoute> {
                LandingPage(
                    onSearch = onSearch,
                    uiState = uiState,
                    onOpenProfile = onOpenProfile,
                    onOpenTopic = onTopicExpand,
                    openFYP = onOpenFYP
                )
            }
            composable<CreateScreenRoute> {
            }
        }


    }

}

@Preview
@Composable
private fun HomeScreenPrev() {
    SageTheme {
//        HomeScreen()
    }
}
