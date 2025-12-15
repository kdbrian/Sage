package com.kdbrian.sage.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kdbrian.sage.ui.screens.GetStarted
import com.kdbrian.sage.ui.screens.HomeScreen
import com.kdbrian.sage.ui.screens.ProfileScreen
import com.sage.datastore.AppDataStore
import com.sage.library.ui.state.TopicDetailsViewModel
import com.sage.ui.composables.onboarding.Screens
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNav() {

    val navController = rememberNavController()
    val topicDetailsViewModel = koinViewModel<TopicDetailsViewModel>()
    val appDataStore = koinInject<AppDataStore>()
    val firstTime = produceState(false) {
        value = appDataStore.firstTime()
    }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = if (firstTime.value) OnboardingRoute else HomeScreenRoute
    ) {

        composable<OnboardingRoute> {
            Screens.WelcomeScreen(
                onDone = {
                    coroutineScope.launch {
                        navController.navigate(HomeScreenRoute)
                        appDataStore.updateFirstTime(false)
                    }
                }
            )
        }

        composable<HomeScreenRoute> {
            HomeScreen(
                onSearch = {
                    navController.navigate(SearchResultsRoute(it))
                },
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

            LaunchedEffect(detailsRoute.topicId) {
                topicDetailsViewModel.loadTopicDetails(detailsRoute.topicId)
            }

//            TopicDetails(
//                topicDetailsViewModel = topicDetailsViewModel,
//                navController = navController,
//                onClose = { navController.popBackStack() }
//            )
        }

        composable<ProfileRoute> {
            ProfileScreen(
                navHostController = navController
            )
        }


        composable<GetStartedRoute> {
            GetStarted()
        }


    }


}