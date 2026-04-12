package com.kdbrian.sage.presentation.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.presentation.ui.theme.LocalFontFamily
import com.kdbrian.sage.presentation.ui.theme.Lora
import com.kdbrian.sage.presentation.ui.theme.PageBg
import com.kdbrian.sage.presentation.ui.theme.RecordBadge
import com.kdbrian.sage.presentation.ui.theme.SageTheme

@Composable
fun ActionButton(
    containerColor: Color = RecordBadge,
    contentColor: Color = PageBg,
    size: Dp = 56.dp,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = containerColor,
        shadowElevation = 8.dp,
        modifier = Modifier.size(size)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun ActionButtonPrev() {
    SageTheme {
        ActionButton() {
            Text(
                "Some text",
                fontFamily = LocalFontFamily.current,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
