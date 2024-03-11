package io.github.junrdev.sage.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.github.junrdev.sage.R
import io.github.junrdev.sage.SplashScreen
import io.github.junrdev.sage.activities.fragments.FilterResult
import io.github.junrdev.sage.model.Favourite
import io.github.junrdev.sage.util.Constants
import io.github.junrdev.sage.util.Constants.auth
import io.github.junrdev.sage.util.Constants.favours
import io.github.junrdev.sage.util.Constants.favs

private const val TAG = "HomeScreen"

class HomeScreen : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

    }

    fun openDownloads(view: View) {
        startActivity(Intent(this, Downloads::class.java))
    }

    fun openUploads(view: View) {
        startActivity(Intent(this, Upload::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout -> {
                auth.signOut()
                startActivity(Intent(this, SplashScreen::class.java))
                finish()
                true
            }

            R.id.account -> {
                Toast.makeText(applicationContext, "Notify", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    fun openFavourites(view: View) {
        if (favs.isEmpty()) {
            Toast.makeText(applicationContext, "No items added.", Toast.LENGTH_SHORT).show()
            return
        }

        val favs = Intent(this, FilterResult::class.java)
        favs.putExtra("content", "favs")
        startActivity(favs)
    }

    fun openHowToSection(view: View) {
        Toast.makeText(applicationContext, "Under Implementation", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (favs.isNotEmpty()) {
            favs.forEach {
                val id = favours.document().id
                favours.document(id)
                    .set(Favourite(id, auth.uid!!, it.fileDownloadUrl, it.fileName))
                    .addOnCompleteListener {
                        if (it.isSuccessful && it.isComplete)
                            Log.d(TAG, "onBackPressed: added fav")
                    }.addOnFailureListener { e ->
                        Log.d(
                            TAG,
                            "onBackPressed: ${e.localizedMessage}"
                        )
                    }
            }
        }
    }

    fun sendFeedback(view: View) {
        val url =
            "https://docs.google.com/forms/d/e/1FAIpQLSc9DTiMHrTNeOT95hrjwc5ybN1Cj_X72wtYPRj2-uhI3RJNiQ/viewform?usp=sf_link"
        val form = Intent(Intent.ACTION_VIEW)
        form.data = Uri.parse(url)
        startActivity(form)
    }
}