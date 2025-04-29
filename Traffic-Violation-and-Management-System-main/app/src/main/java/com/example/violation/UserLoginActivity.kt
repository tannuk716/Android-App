package com.example.violation

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.shashank.sony.fancytoastlib.FancyToast

class UserLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val etUsername = findViewById<EditText>(R.id.etUserUsername)
        val etPassword = findViewById<EditText>(R.id.etUserPassword)
        val btnLogin = findViewById<Button>(R.id.btnUserLogin)
        val tvSignup = findViewById<TextView>(R.id.btnsignup)

        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val savedUsername = sharedPref.getString("username", null)
        val savedPassword = sharedPref.getString("password", null)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                FancyToast.makeText(this, "Please enter both fields", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show()
            } else if (username == savedUsername && password == savedPassword) {
                FancyToast.makeText(this, "Login successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show()

                val sessionManager = SessionManager(this)
                sessionManager.saveUserId(username)

                val intent = Intent(this, UserDashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                FancyToast.makeText(this, "Invalid credentials", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show()
            }
        }

        tvSignup.setOnClickListener {
            val intent = Intent(this, UserSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
