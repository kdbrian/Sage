package io.github.junrdev.sage.presentation.screens.features.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.junrdev.sage.R
import io.github.junrdev.sage.ui.theme.SageTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Preview
@Composable
private fun SignInSignUpPrev() {
    SageTheme {
        SignInSignUp()
    }
}

@Composable
fun SignInSignUp(modifier: Modifier = Modifier) {


    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val googleLogin: () -> Unit = {
        scope.launch {
            isLoading = true
            delay(1_200)
            isLoading = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.people),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Surface(
            content = {},
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f),
            color = Color.Black
        )


        if (!isLoading) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 12.dp),
                onClick = googleLogin,
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = BorderStroke(width = 1.dp, color = Color.White)
            ) {
                Image(
                    painter = painterResource(R.drawable.continue_with_google),
                    contentDescription = "continue with google",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                )
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        Text(
            text = "All rights reserved.",
            style = MaterialTheme.typography.labelLarge,
            color = Color.LightGray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )

    }
}


