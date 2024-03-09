package io.github.junrdev.sage.features.imagetotext

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import io.github.junrdev.sage.R
import io.github.junrdev.sage.model.FileItem

class NetworkImageToText : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_image_to_text)
    }

    fun convertImageContent(fileItem: FileItem){
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)



    }
}