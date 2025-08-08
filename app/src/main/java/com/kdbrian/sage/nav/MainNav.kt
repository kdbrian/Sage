package com.kdbrian.sage.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kdbrian.sage.ui.screens.DocumentDetails
import com.kdbrian.sage.ui.screens.GetStarted
import com.kdbrian.sage.ui.screens.HomeScreen
import com.kdbrian.sage.ui.screens.ProfileScreen
import com.kdbrian.sage.ui.screens.TopicDetails

@Composable
fun MainNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeScreenRoute) {

        composable<HomeScreenRoute> {
            HomeScreen(
                onOpenProfile = {
                    navController.navigate(ProfileRoute)
                },
                onTopicExpand = {
                    navController.navigate(TopicDetailsRoute)
                },
                onOpenFYP = {
                }
            )
        }


        composable<TopicDetailsRoute> {
            TopicDetails(
                navController = navController,
                onClose = {navController.popBackStack()}
            )
        }

        composable<ProfileRoute> {
            ProfileScreen(
                navHostController = navController
            )
        }

        composable<DocumentDetailsRoute> {
            val detailsRoute = it.toRoute<DocumentDetailsRoute>()
            DocumentDetails(
                navHostController = navController,
                documentId = detailsRoute.docId

            )
        }

        composable<GetStartedRoute>{
            GetStarted()
        }

    }


}