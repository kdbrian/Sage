package com.kdbrian.sage.nav

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kdbrian.sage.ui.screens.DocumentDetails
import com.kdbrian.sage.ui.screens.GetStarted
import com.kdbrian.sage.ui.screens.HomeScreen
import com.kdbrian.sage.ui.screens.ProfileScreen
import com.kdbrian.sage.ui.screens.TopicDetails
import com.kdbrian.sage.ui.state.TopicDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNav() {

    val navController = rememberNavController()
    val topicDetailsViewModel = koinViewModel<TopicDetailsViewModel>()


    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = HomeScreenRoute
    ) {

        composable<HomeScreenRoute> {
            HomeScreen(
                onOpenProfile = {
                    navController.navigate(ProfileRoute)
                },
                onTopicExpand = {
                    navController.navigate(TopicDetailsRoute(it))
                },
                onOpenFYP = {
                }
            )
        }


        composable<TopicDetailsRoute> {
            val detailsRoute = it.toRoute<TopicDetailsRoute>()

            LaunchedEffect(Unit) {
                topicDetailsViewModel.loadTopicDetails(detailsRoute.topicId)
            }

            TopicDetails(
                topicDetailsViewModel = topicDetailsViewModel,
                navController = navController,
                onClose = { navController.popBackStack() }
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

        composable<GetStartedRoute> {
            GetStarted()
        }

    }


}