package com.sage.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sage.ui.R

// Set of Material typography styles to start with
val Ubuntu by lazy {
    FontFamily(
        Font(R.font.ubuntu_bold,),
        Font(R.font.ubuntu_light),
        Font(R.font.ubuntu_regular),
        Font(R.font.ubuntu_medium),
        Font(R.font.ubuntu_bolditalic),
        Font(R.font.ubuntu_lightitalic),
        Font(R.font.ubuntu_mediumitalic),
        Font(R.font.ubuntu_italic),
    )
}


val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)