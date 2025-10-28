package com.kdbrian.sage

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kdbrian.sage.nav.MainNav
import com.sage.ui.theme.SageTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        //create notification channel for android 14 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    applicationContext.getString(R.string.default_notification_channel),
                    "Sage Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }



        setContent {
            App {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permissionState =
                        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
                    LaunchedEffect(Unit) {
                        if (!permissionState.status.isGranted) {
                            permissionState.launchPermissionRequest()
                        }
                    }
                }



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