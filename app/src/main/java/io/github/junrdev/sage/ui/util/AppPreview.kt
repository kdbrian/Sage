package io.github.junrdev.sage.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.junrdev.sage.presentation.screens.features.auth.OnBoardingScreen
import io.github.junrdev.sage.presentation.screens.features.auth.OnBoardingScreenPager
import io.github.junrdev.sage.ui.theme.SageTheme


@Preview
@Composable
private fun OnBoardingPreview() {
    SageTheme {
//        OnBoardingScreenPager()
        OnBoardingScreen()
    }
}
