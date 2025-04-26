package com.example.violation

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast

class UserSignUp : AppCompatActivity() {
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        sharedPrefManager = SharedPrefManager.getInstance(this)

        val etName = findViewById<TextInputEditText>(R.id.etUserName)
        val etEmail = findViewById<TextInputEditText>(R.id.etUserEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etUserPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.ConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnUserSignup)
        val tvLoginRedirect = findViewById<TextView>(R.id.tvLoginRedirect)

        btnSignUp.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                    showToast("Please fill all fields", FancyToast.ERROR)
                !isValidEmail(email) ->
                    showToast("Please enter a valid email", FancyToast.WARNING)
                password.length < 6 ->
                    showToast("Password must be at least 6 characters", FancyToast.WARNING)
                password != confirmPassword ->
                    showToast("Passwords don't match", FancyToast.WARNING)
                sharedPrefManager.doesEmailExist(email) ->
                    showToast("Email already registered", FancyToast.WARNING)
                else -> {
                    sharedPrefManager.saveUserCredentials(name, email)
                    sharedPrefManager.saveUserPassword(password)
                    showToast("Registration successful!", FancyToast.SUCCESS)
                    startActivity(Intent(this, UserLoginActivity::class.java))
                    finish()
                }
            }
        }

        tvLoginRedirect.setOnClickListener {
            startActivity(Intent(this, UserLoginActivity::class.java))
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String, type: Int) {
        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, type, true).show()
    }
}
