package com.example.violation

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.sony.fancytoastlib.FancyToast

class ViewViolationsActivity : AppCompatActivity() {

    private lateinit var tvViolations: TextView
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_violations)

        // Initialize SharedPrefManager using the singleton instance
        sharedPrefManager = SharedPrefManager.getInstance(this)
        tvViolations = findViewById(R.id.tvViolations)

        val userId = sharedPrefManager.getUserId()
        if (userId.isNotEmpty()) {
            showUnpaidFines(userId)
        } else {
            tvViolations.text = "User not logged in"
            FancyToast.makeText(
                this,
                "Please login to view violations",
                FancyToast.LENGTH_SHORT,
                FancyToast.WARNING,
                true
            ).show()
            finish()
        }
    }

    private fun showUnpaidFines(userId: String) {
        try {
            val sharedPref = getSharedPreferences("FINE_DATA", Context.MODE_PRIVATE)
            val gson = Gson()
            val finesJson = sharedPref.getString("fines_$userId", "[]") ?: "[]"
            val type = object : TypeToken<List<Violation>>() {}.type
            val fines: List<Violation> = gson.fromJson(finesJson, type)

            val unpaidFines = fines.filter { !it.paid }

            if (unpaidFines.isEmpty()) {
                tvViolations.text = "No unpaid violations found"
                FancyToast.makeText(
                    this,
                    "No violations to display",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    true
                ).show()
            } else {
                val result = buildString {
                    unpaidFines.forEachIndexed { index, violation ->
                        append("Violation #${index + 1}\n")
                        append("ID: ${violation.id}\n")
                        append("Type: ${violation.type}\n")
                        append("Amount: ₹${violation.amount}\n")
                        append("Date: ${violation.date}\n")
                        append("Status: ${if (violation.paid) "PAID" else "UNPAID"}\n\n")
                    }
                }
                tvViolations.text = result
            }
        } catch (e: Exception) {
            tvViolations.text = "Error loading violations"
            FancyToast.makeText(
                this,
                "Failed to load violation data",
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                true
            ).show()
        }
    }

    // Data class for violations
    data class Violation(
        val id: String,
        val type: String,
        val amount: Double,
        val date: String,
        var paid: Boolean = false
    )
}