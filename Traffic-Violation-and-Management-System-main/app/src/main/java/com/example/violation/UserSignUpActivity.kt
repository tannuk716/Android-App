package com.example.violation

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class UserSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        val etUsername = findViewById<EditText>(R.id.etUserUsername)
        val etPassword = findViewById<EditText>(R.id.etUserPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etconfermUserPassword)
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val btnSignup = findViewById<Button>(R.id.btnUserLogin)
        val tvLogin = findViewById<TextView>(R.id.btnlogin)

        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        btnSignup.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val fullName = etFullName.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Save the username and full name
                val sessionManager = SessionManager(this)
                sessionManager.saveUserId(username)  // Save user ID
                sessionManager.saveFullName(fullName)  // Save full name

                Toast.makeText(this, "Sign-up successful", Toast.LENGTH_SHORT).show()

                // Go to Login screen
                val intent = Intent(this, UserLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }
    }
}
