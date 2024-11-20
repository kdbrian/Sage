package io.github.junrdev.sage.presentation.screens.features.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier) {

    val pagerItems = listOf(
        OnBoardingScreenItem(title = "Lorem ipsum dolor 1"),
        OnBoardingScreenItem(title = "Lorem ipsum dolor 2"),
        OnBoardingScreenItem(title = "Lorem ipsum dolor 3"),
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pagerItems.size })
    val scope = rememberCoroutineScope()


    val pagerButtonOnclick: () -> Unit = {
        scope.launch {
            if (pagerState.canScrollForward)
                pagerState.scrollToPage(pagerState.currentPage + 1)
            else
                pagerState.scrollToPage(pagerState.currentPage - 1)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        HorizontalPager(state = pagerState) {
            val current = pagerItems[it]
            OnBoardingScreenPager(
                onBoardingScreenItem = current,
                pagerButtonOnclick = pagerButtonOnclick
            )
        }

    }
}


@Composable
fun OnBoardingScreenPager(
    modifier: Modifier = Modifier,
    onBoardingScreenItem: OnBoardingScreenItem = OnBoardingScreenItem(),
    pagerButtonOnclick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = onBoardingScreenItem.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )

            Text(
                text = onBoardingScreenItem.description,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = { pagerButtonOnclick() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Get Started")
        }
    }
}


open class OnBoardingScreenItem(
    open val title: String = "Lorem ipsum dolor",
    open val description: String = "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
)
