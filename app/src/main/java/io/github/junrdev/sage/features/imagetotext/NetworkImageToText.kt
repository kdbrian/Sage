package io.github.junrdev.sage.features.imagetotext

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.squareup.picasso.Picasso
import io.github.junrdev.sage.R
import io.github.junrdev.sage.model.FileItem
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime

private const val TAG = "NetworkImageToText"

class NetworkImageToText : AppCompatActivity() {

    private lateinit var desc : TextView
    private lateinit var img : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_image_to_text)
        desc = findViewById(R.id.desc)
        img = findViewById(R.id.img)

    }

    fun downloadImage(fileItem: FileItem){

        Picasso.get()
            .load(fileItem.fileDownloadUrl)
            .into(img)

        val client = OkHttpClient()

        val req = Request.Builder()
            .get()
            .url(fileItem.fileDownloadUrl)
            .build()

        client.newCall(req)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "onFailure: Failed ${e.localizedMessage}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){

                        Log.d(TAG, "onResponse: ${response.body}")

                        val localfile = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            fileItem.fileName
                        )

                        FileOutputStream(localfile).use { output->
                            response.body?.byteStream().use {input ->
                                input?.copyTo(output)
                            }
                        }

                        convertImageContent(localfile.toUri())
                        Log.d(TAG, "onResponse: Done")
                    }
                }
            })
    }

    fun convertImageContent(url : Uri) {
        val image : InputImage
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        image = InputImage.fromFilePath(applicationContext, url)

        recognizer.process(image)
            .addOnCompleteListener {
                if (it.isComplete && it.isSuccessful){
                    Log.d(TAG, "convertImageContent: ${it.result}")
                    desc.text = "${LocalDateTime.now()} ${it.result?.text}"
                }
            }
            .addOnFailureListener{e -> Log.d(TAG, "convertImageContent: ${e.localizedMessage}")}

    }
}