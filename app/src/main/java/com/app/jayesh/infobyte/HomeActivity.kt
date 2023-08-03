package com.app.jayesh.infobyte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    lateinit var btnLogout: Button
    lateinit var tvWelcome: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnLogout = findViewById(R.id.btnLogout)
        tvWelcome = findViewById(R.id.tvWelcome)
        auth = FirebaseAuth.getInstance()

        tvWelcome.text = "Welcome, ${auth.currentUser?.phoneNumber}"
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
}