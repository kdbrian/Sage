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
import com.kdbrian.sage.ui.theme.SageTheme
import com.kdbrian.sage.ui.util.BottomBarItem
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    onOpenProfile : ()-> Unit,
    onTopicExpand : (String)-> Unit,
    onOpenFYP: () -> Unit = {}
) {

    val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()
    val uiState by homeScreenViewModel.uiState.collectAsState()
    val backStackEntryAsState = navHostController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomBarItem.twoScreenItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = backStackEntryAsState.value?.destination?.route == item.route.toString(),
                        onClick = {
                            navHostController.navigate(item.route){
                                popUpTo(HomeScreenRoute){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navHostController,
            startDestination = HomeScreenRoute
        ) {
            composable<HomeScreenRoute> {
                LandingPage(
                    uiState = uiState,
                    onOpenProfile = onOpenProfile,
                    onOpenTopic = onTopicExpand,
                    openFYP = onOpenFYP
                )
            }
            composable<CreateScreenRoute> {
                CreateScreen()
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
