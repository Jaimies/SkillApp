package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null) {

            gotoMainActivity()

            return

        }

        setContentView(R.layout.activity_login)
    }


    private fun gotoMainActivity() {

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

        finish()

    }
}
