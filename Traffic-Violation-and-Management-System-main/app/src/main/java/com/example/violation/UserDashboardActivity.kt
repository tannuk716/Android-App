package com.example.violation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        // Initialize buttons
        val btnViewViolations = findViewById<Button>(R.id.btnViewViolations)
        val btnPayFine = findViewById<Button>(R.id.btnPayFine)
        val btnViolationHistory = findViewById<Button>(R.id.btnViolationHistory)

        // Initialize Bottom Navigation View
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set default selected item
        bottomNav.selectedItemId = R.id.nav_dashboard

        // Set item selected listener for bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> true  // Do nothing if the current item is selected
                R.id.nav_profile -> {
                    // Navigate to user profile activity
                    startActivity(Intent(this, UserProfile::class.java))
                    overridePendingTransition(0, 0)  // Prevent animation transition
                    true
                }
                else -> false
            }
        }

        // Set click listeners for each button
        btnViewViolations.setOnClickListener {
            // Navigate to View Violations Activity
            val intent = Intent(this, ViewViolationsActivity::class.java)
            startActivity(intent)
        }

        btnPayFine.setOnClickListener {
            // Navigate to Pay Fine Activity
            val intent = Intent(this, PayFineActivity::class.java)
            startActivity(intent)
        }

        btnViolationHistory.setOnClickListener {
            // Navigate to Violation History Activity
            val intent = Intent(this, ViolationHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
