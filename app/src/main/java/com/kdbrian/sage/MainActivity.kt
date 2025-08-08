package com.kdbrian.sage

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import com.kdbrian.sage.nav.MainNav
import com.kdbrian.sage.ui.theme.SageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            enableEdgeToEdge()
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        setContent {
            App {
                MainNav()
            }
        }
    }
}


@Composable
fun App(content: @Composable () -> Unit) {
    SageTheme {
        content()
    }
}