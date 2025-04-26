package com.example.violation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast

class UserLoginActivity : AppCompatActivity() {
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignupRedirect: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        sharedPrefManager = SharedPrefManager.getInstance(this)

        etUsername = findViewById(R.id.etUserUsername)
        etPassword = findViewById(R.id.etUserPassword)
        btnLogin = findViewById(R.id.btnUserLogin)
        tvSignupRedirect = findViewById(R.id.tvSignupRedirect)

        if (sharedPrefManager.isLoggedIn()) {
            redirectToDashboard()
            return
        }

        btnLogin.setOnClickListener {
            handleLoginAttempt()
        }

        tvSignupRedirect.setOnClickListener {
            navigateToSignup()
        }
    }

    private fun handleLoginAttempt() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        when {
            username.isEmpty() -> {
                etUsername.error = "Email required"
                etUsername.requestFocus()
            }
            password.isEmpty() -> {
                etPassword.error = "Password required"
                etPassword.requestFocus()
            }
            else -> authenticateUser(username, password)
        }
    }

    private fun authenticateUser(username: String, password: String) {
        sharedPrefManager.getUserCredentials()?.let { credentials ->
            val storedEmail = credentials.second
            if (username == storedEmail && password == sharedPrefManager.getUserPassword()) {
                sharedPrefManager.saveSession(credentials.first, "user", "user_auth_token")
                showToast("Login successful", FancyToast.SUCCESS)
                redirectToDashboard()
            } else {
                showToast("Invalid credentials", FancyToast.ERROR)
            }
        } ?: showToast("No registered user found", FancyToast.WARNING)
    }

    private fun redirectToDashboard() {
        startActivity(Intent(this, UserDashboardActivity::class.java))
        finish()
    }

    private fun navigateToSignup() {
        startActivity(Intent(this, UserSignUp::class.java))
        finish()
    }

    private fun showToast(message: String, type: Int) {
        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, type, true).show()
    }
}
