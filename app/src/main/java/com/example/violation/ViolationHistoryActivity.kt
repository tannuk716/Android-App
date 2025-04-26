package com.example.violation

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.sony.fancytoastlib.FancyToast

class ViolationHistoryActivity : AppCompatActivity() {

    private lateinit var tvHistory: TextView
    private lateinit var sharedPrefManager: SharedPrefManager

    // Data class for fines
    data class Fine(
        val fineId: String,
        val violation: String,
        val amount: Double,
        val date: String,
        var paid: Boolean = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_violation_history)

        // Initialize SharedPrefManager using singleton pattern
        sharedPrefManager = SharedPrefManager.getInstance(this)
        tvHistory = findViewById(R.id.tvHistory)

        val userId = sharedPrefManager.getUserId()
        if (userId.isNotEmpty()) {
            showPaidFines(userId)
        } else {
            tvHistory.text = "User not logged in"
            FancyToast.makeText(
                this,
                "Please login to view history",
                FancyToast.LENGTH_SHORT,
                FancyToast.WARNING,
                true
            ).show()
            finish()
        }
    }

    private fun showPaidFines(userId: String) {
        try {
            val sharedPref = getSharedPreferences("FINE_DATA", Context.MODE_PRIVATE)
            val gson = Gson()
            val finesJson = sharedPref.getString("fines_$userId", "[]") ?: "[]"
            val type = object : TypeToken<List<Fine>>() {}.type
            val fines: List<Fine> = gson.fromJson(finesJson, type)

            val paidFines = fines.filter { it.paid }

            if (paidFines.isEmpty()) {
                tvHistory.text = "No violation history found"
                FancyToast.makeText(
                    this,
                    "No payment history available",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    true
                ).show()
            } else {
                val result = buildString {
                    paidFines.forEachIndexed { index, fine ->
                        append("Payment #${index + 1}\n")
                        append("ID: ${fine.fineId}\n")
                        append("Violation: ${fine.violation}\n")
                        append("Amount Paid: ₹${fine.amount}\n")
                        append("Date: ${fine.date}\n\n")
                    }
                }
                tvHistory.text = result
            }
        } catch (e: Exception) {
            tvHistory.text = "Error loading payment history"
            FancyToast.makeText(
                this,
                "Failed to load payment data",
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                true
            ).show()
        }
    }
}