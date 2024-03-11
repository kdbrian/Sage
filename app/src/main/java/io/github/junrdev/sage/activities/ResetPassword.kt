package io.github.junrdev.sage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import io.github.junrdev.sage.R
import io.github.junrdev.sage.util.Constants.auth

private const val TAG = "ResetPassword"

class ResetPassword : AppCompatActivity() {

    private lateinit var email: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        email = findViewById(R.id.email)

    }

    fun sendResetEmail(view: View) {
        if (email.text.toString().isEmpty())
            Toast.makeText(applicationContext, "No email provided.", Toast.LENGTH_SHORT).show()
        else {
            auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
                if (it.isComplete && it.isSuccessful) {
                    Toast.makeText(applicationContext, "Check your email.", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "Failed due to ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()

                Log.d(TAG, "sendResetEmail: ${e.localizedMessage}")


            }
        }
    }
}