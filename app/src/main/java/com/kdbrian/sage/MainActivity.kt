package com.kdbrian.sage

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.kdbrian.sage.domain.model.TopicItem
import com.kdbrian.sage.nav.MainNav
import com.kdbrian.sage.ui.theme.SageTheme
import com.kdbrian.sage.util.AppDataStore
import com.kdbrian.sage.util.NotificationData
import com.kdbrian.sage.util.NotificationUtils.showNotification
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import timber.log.Timber
import kotlin.jvm.Throws
import kotlin.random.Random

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

                val firebaseFirestore = koinInject<FirebaseFirestore>()
                val appDataStore = koinInject<AppDataStore>()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permissionState =
                        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
                    LaunchedEffect(Unit) {
                        if (!permissionState.status.isGranted) {
                            permissionState.launchPermissionRequest()
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    if (appDataStore.firstTime.first()){ //if is first time
                        uploadDefaultTopics(firebaseFirestore) {

                            val data = NotificationData(
                                channelId = applicationContext.getString(R.string.default_notification_channel),
                                title = if (it != null) "Error" else "Success",
                                message = it?.message ?: "Uploaded to firestore done.",
                                smallIcon = R.drawable.outline_brightness_alert_24
                            )

                            applicationContext.showNotification(
                                data = data,
                                notificationId = Random.nextInt(1, 9)
                            )


                        }
                        appDataStore.updateFirstTime(false);
                    }
                }


                MainNav()
            }
        }

    }

    fun uploadDefaultTopics(db: FirebaseFirestore, onCompleteListener: (Throwable?) -> Unit = {}) {
        val topics = listOf(
            "Business",
            "Finance",
            "Lifestyle",
            "Technology",
            "Health",
            "Travel",
            "Food",
            "Education",
            "Sports",
            "Science",
            "Entertainment",
            "Politics",
            "Environment",
            "Art",
            "Music",
            "History",
            "Fashion",
            "Relationships",
            "Self Improvement",
            "Real Estate"
        ).map { topicName ->
            TopicItem(topicName = topicName)
        }

        val batch = db.batch()
        val topicsRef = db.collection(TopicItem.collectionName)

        topics.forEach { topic ->
            val docRef = topicsRef.document()
            val topicWithId = topic.copy(topicId = docRef.id) // set topicId to Firestore doc id
            batch.set(docRef, topicWithId)
        }

        batch.commit()
            .addOnSuccessListener {
                Timber.d("Firestore All topics uploaded successfully")
                onCompleteListener(null)
            }
            .addOnFailureListener { e ->
                Timber.e("Firestore Error uploading topics ${e.localizedMessage}")
                onCompleteListener(e)
            }
    }


}


@Composable
fun App(content: @Composable () -> Unit) {
    SageTheme {
        content()
    }
}