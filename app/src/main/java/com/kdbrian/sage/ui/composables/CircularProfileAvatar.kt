package com.kdbrian.sage.ui.composables

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kdbrian.sage.R
import com.kdbrian.sage.ui.theme.SageTheme

@Composable
fun CircularProfileAvatar(modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier
            .requiredSize(50.dp),
        shape = CircleShape
    ) {
        AsyncImage(
            model = null,
            placeholder = painterResource(R.drawable.current_premium),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

    }
}

@Preview
@Composable
private fun CircularProfileAvatarPrev() {
    SageTheme {
        CircularProfileAvatar()
    }
}