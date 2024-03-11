package io.github.junrdev.sage.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.datatransport.runtime.dagger.Binds
import io.github.junrdev.sage.R
import io.github.junrdev.sage.activities.fragments.UploadDocuments
import io.github.junrdev.sage.activities.fragments.UploadImages
import io.github.junrdev.sage.model.FileItem
import io.github.junrdev.sage.util.Constants
import java.util.stream.Collectors

private const val TAG = "Upload"

class Upload : AppCompatActivity() {

    val files: MutableList<FileItem> = mutableListOf()

    private lateinit var selectedImgesCount : TextView
    private lateinit var selectedFilesCount : TextView
    private lateinit var scannedItemsCount : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        selectedImgesCount = findViewById(R.id.selectedImgesCount)
        scannedItemsCount = findViewById(R.id.scannedItemsCount)
        selectedFilesCount = findViewById(R.id.selectedFilesCount)

        Constants.filesmetadata.get().addOnCompleteListener {
            if (it.isSuccessful && it.isComplete) {

                if (it.result.hasChildren()) {
                    it.result.children.forEach { snap ->
                        if (snap.exists()) {
                            files.add(snap.getValue(FileItem::class.java)!!)
                        }
                    }

                    if (files.isNotEmpty()){
                        val images = files.stream().filter{file -> file.categories.contains("images")}.collect(Collectors.toList()).size
                        val ufiles = files.stream().filter{file -> file.categories.contains("pdf")}.collect(Collectors.toList()).size
                        val scanned = files.stream().filter{file -> file.categories.contains("scanned")}.collect(Collectors.toList()).size

                        selectedImgesCount.text = "$images"
                        scannedItemsCount.text = "$scanned"
                        selectedFilesCount.text = "$ufiles"
                    }
                }
            }
        }.addOnFailureListener { e -> Log.d(TAG, "onCreate: ${e.localizedMessage}") }
    }

    fun openUploadImages(view: View) {
        val uploadImages = Intent(this, UploadImages::class.java)
        uploadImages.putExtra("type", "gallery")
        startActivity(uploadImages)
    }

    fun openUploadFiles(view: View) {
        val uploadImages = Intent(this, UploadDocuments::class.java)
        uploadImages.putExtra("type", "gallery")
        startActivity(uploadImages)
    }

    fun openUploadCamera(view: View) {
        val openCamera = Intent(this, UploadImages::class.java)
        openCamera.putExtra("type", "camera")
        startActivity(openCamera)
    }
}