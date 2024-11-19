package io.github.junrdev.sage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.ai.client.generativeai.GenerativeModel
import io.github.junrdev.sage.data.remote.repoImpl.FirebaseStorageDocumentsRepoImpl
import io.github.junrdev.sage.data.remote.repoImpl.GenerativeAIRepoImpl
import io.github.junrdev.sage.domain.repo.FirebaseStorageDocumentsRepo
import io.github.junrdev.sage.ui.theme.SageTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val testRepo by inject<FirebaseStorageDocumentsRepo>()

        setContent {
            SageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SageTheme {
        Greeting("Android")
    }
}