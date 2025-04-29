package com.example.violation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserProfile : AppCompatActivity() {

    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        sharedPrefManager = SharedPrefManager.getInstance(this)


        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val btnSignOut = findViewById<Button>(R.id.btnSignOut)
        val profileImage = findViewById<ImageView>(R.id.profile_image)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)



         val ( email) = sharedPrefManager.getProfileDisplayData()

         tvEmail.text = "Email: $email"

        // Sign out button
        btnSignOut.setOnClickListener {
            sharedPrefManager.clearSession()
            startActivity(Intent(this, UserLoginActivity::class.java))
            finish()
        }

        // Profile image click
        profileImage.setOnClickListener {
            startActivity(Intent(this, UserDashboardActivity::class.java))
        }

        // Bottom navigation
        bottomNav.selectedItemId = R.id.nav_profile
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, UserDashboardActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }
}