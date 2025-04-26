package com.example.violation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.shashank.sony.fancytoastlib.FancyToast

class AdminLoginActivity : AppCompatActivity() {
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        sharedPrefManager = SharedPrefManager.getInstance(this)

        // Auto-redirect if already logged in as admin
        if (sharedPrefManager.isLoggedIn() && sharedPrefManager.getUserRole() == "admin") {
            redirectToAdminDashboard()
            return
        }

        val etUsername = findViewById<EditText>(R.id.etAdminUsername)
        val etPassword = findViewById<EditText>(R.id.etAdminPassword)
        val btnLogin = findViewById<Button>(R.id.btnAdminLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                username.isEmpty() || password.isEmpty() ->
                    showToast("Please enter both fields", FancyToast.ERROR)

                sharedPrefManager.isValidAdmin(username, password) ->
                    handleAdminLogin(username)

                else ->
                    showToast("Invalid admin credentials", FancyToast.ERROR)
            }
        }
    }

    private fun handleAdminLogin(username: String) {
        sharedPrefManager.saveSession(username, "admin", "admin_token")
        showToast("Admin login successful", FancyToast.SUCCESS)
        redirectToAdminDashboard()
    }

    private fun redirectToAdminDashboard() {
        startActivity(Intent(this, AdminDashboardActivity::class.java))
        finish() // Prevent going back to login screen
    }

    private fun showToast(message: String, type: Int) {
        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, type, true).show()
    }
}