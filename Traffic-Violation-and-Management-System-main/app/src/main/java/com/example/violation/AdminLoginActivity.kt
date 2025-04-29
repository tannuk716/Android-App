package com.example.violation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.shashank.sony.fancytoastlib.FancyToast

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val etUsername = findViewById<EditText>(R.id.etAdminUsername)
        val etPassword = findViewById<EditText>(R.id.etAdminPassword)
        val btnLogin = findViewById<Button>(R.id.btnAdminLogin)

        val adminCredentials = mapOf(
            "vikasyadav2412" to "Vik@s2412"

        )

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (adminCredentials[username] == password) {
                // Save login session
                SessionManager(this).saveAuthToken("generated_token_here", "admin")

                FancyToast.makeText(this, "Login successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show()
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()

            }else {
                FancyToast.makeText(this, "Invalid credentials", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show()
            }
        }
    }
}