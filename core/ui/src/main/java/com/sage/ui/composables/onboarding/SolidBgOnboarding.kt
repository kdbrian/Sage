package com.sage.ui.composables.onboarding

import androidx.annotation.ColorLong
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sage.ui.R
import com.sage.ui.composables.locals.LocalFontFamily
import com.sage.ui.theme.SageTheme
import kotlinx.coroutines.launch

object Screens {

    data class SolidOnboardingItem(
        val title: String,
        val supportText: String,
        val label: String,
        @param:DrawableRes val image: Int,
        @param:ColorLong val color: Long,
    )

    val defaultItems = listOf(
        SolidOnboardingItem(
            label = "Become a \nSage today",
            title = "What is to sage?",
            supportText = "profoundly wise.\nthey nodded in agreement with these sage remarks",
            color = 0xFFA4243B,
            image = R.drawable.sage_master
        ),
        SolidOnboardingItem(
            label = "Create your mind",
            title = "Mind mapping.",
            supportText = "Through mind mapping, you feed your knowledge base what you want.",
            color = 0xFFBD632F,
            image = R.drawable.mind_map
        ),
        SolidOnboardingItem(
            label = "Join others",
            title = "Welcome others and join there party?",
            supportText = "Through parties rich bases are created.",
            color = 0xFFF2F7F2,
            image = R.drawable.outsource_mind
        ),
    )

    @Composable
    fun WelcomeScreen(
        onDone: () -> Unit = {},
        items: List<SolidOnboardingItem> = defaultItems
    ) {

        val pagerState = rememberPagerState { items.size }
        val coroutineScope = rememberCoroutineScope()
        val currentColor = remember {
            derivedStateOf {
                Color(items[pagerState.currentPage].color)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = currentColor.value,
                )
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Text(
                        text = items[pagerState.currentPage].label,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontFamily = LocalFontFamily.current,
                            fontWeight = FontWeight.Bold,
                        )
                    )

                    Image(
                        painter = painterResource(items[pagerState.currentPage].image),
                        contentDescription = items[pagerState.currentPage].label,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .clip(RectangleShape),
                        contentScale = ContentScale.Fit
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = items[pagerState.currentPage].title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = LocalFontFamily.current,
                                fontWeight = FontWeight.Bold,
                            )
                        )

                        Text(
                            text = items[pagerState.currentPage].supportText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = LocalFontFamily.current,
                                fontWeight = FontWeight.Light,
                            )
                        )

                        Spacer(Modifier
                            .fillMaxWidth()
                            .padding(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(6.dp)
                            ) {

                                repeat(pagerState.pageCount) {
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .background(
                                                color = if (it == pagerState.currentPage) Color.White else currentColor.value,
                                                shape = CircleShape
                                            )
                                            .border(
                                                width = if (it == pagerState.currentPage) 1.dp else 0.dp,
                                                color = Color.Black,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        if (pagerState.canScrollForward)
                                            pagerState.animateScrollToPage(pagerState.currentPage.inc())
                                        else
                                            onDone()
                                    }
                                },
                                shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF273E47),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = if (pagerState.canScrollForward) "Next" else "Start",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = LocalFontFamily.current,
                                    ),
                                    modifier = Modifier
                                )
                            }

                        }

                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                visible = pagerState.canScrollForward
            ) {
                TextButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {

                    }
                ) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = LocalFontFamily.current,
                            color = Color.White
                        )
                    )
                }
            }

        }
    }

}


@androidx.compose.ui.tooling.preview.Preview
@androidx.compose.runtime.Composable
private fun SolidBgOnboardingPrev() {
    SageTheme {
        Screens.WelcomeScreen()
    }
}