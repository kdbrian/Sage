package com.kdbrian.sage.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kdbrian.sage.nav.CreateScreenRoute
import com.kdbrian.sage.nav.HomeScreenRoute
import com.kdbrian.sage.ui.composables.CircularProfileAvatar
import com.kdbrian.sage.ui.composables.RoundedInputField
import com.kdbrian.sage.ui.theme.SageTheme
import com.kdbrian.sage.ui.util.BottomBarItem

@Composable
fun HomeScreen(
    navHostController: NavHostController = rememberNavController(),
    onOpenProfile : ()-> Unit,
    onTopicExpand : ()-> Unit,
    onOpenFYP: () -> Unit = {}
) {

    val backStackEntryAsState = navHostController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomBarItem.twoScreenItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = backStackEntryAsState.value?.destination?.route == item.route.toString(),
                        onClick = {
                            navHostController.navigate(item.route)
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
