package com.example.violation

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewViolationsActivity : AppCompatActivity() {

    private lateinit var tvViolations: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_violations)

        tvViolations = findViewById(R.id.tvViolations)

        val userId = SessionManager(this).getUserId()
        if (userId.isNotEmpty()) {
            showUnpaidFines(userId)
        } else {
            tvViolations.text = "User not logged in"
        }
    }

    private fun showUnpaidFines(userId: String) {
        val sharedPref = getSharedPreferences("FINE_DATA_$userId", Context.MODE_PRIVATE)
        val gson = Gson()
        val finesJson = sharedPref.getString("fines", "[]")
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val fines: List<Map<String, Any>> = gson.fromJson(finesJson, type)

        val unpaidFines = fines.filter { it["paid"] == false }

        if (unpaidFines.isEmpty()) {
            tvViolations.text = "No violations found."
        } else {
            val result = StringBuilder()
            for (fine in unpaidFines) {
                result.append("Fine ID: ${fine["fineId"]}\n")
                result.append("Violation: ${fine["violation"]}\n")
                result.append("Amount: ₹${fine["amount"]}\n\n")
            }
            tvViolations.text = result.toString()
        }
    }
}
