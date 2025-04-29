package com.example.violation

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViolationHistoryActivity : AppCompatActivity() {

    private lateinit var tvHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_violation_history)

        tvHistory = findViewById(R.id.tvHistory)

        val userId = SessionManager(this).getUserId()
        if (userId != null) {
            showPaidFines(userId)
        } else {
            tvHistory.text = "User not logged in"
        }
    }

    private fun showPaidFines(userId: String) {
        val sharedPref = getSharedPreferences("FINE_DATA_$userId", Context.MODE_PRIVATE)
        val gson = Gson()
        val finesJson = sharedPref.getString("fines", "[]")

        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val fines: List<Map<String, Any>> = gson.fromJson(finesJson, type)

        val paidFines = fines.filter { it["paid"] == true }

        if (paidFines.isEmpty()) {
            tvHistory.text = "No violation history found."
        } else {
            val result = StringBuilder()
            for (fine in paidFines) {
                result.append("Fine ID: ${fine["fineId"]}\n")
                result.append("Violation: ${fine["violation"]}\n")
                result.append("Amount Paid: ₹${fine["amount"]}\n\n")
            }
            tvHistory.text = result.toString()
        }
    }
}
