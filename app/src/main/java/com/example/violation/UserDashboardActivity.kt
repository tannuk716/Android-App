package com.example.violation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        val btnViewViolations = findViewById<Button>(R.id.btnViewViolations)
        val btnPayFine = findViewById<Button>(R.id.btnPayFine)
        val btnViolationHistory = findViewById<Button>(R.id.btnViolationHistory)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        btnViewViolations.setOnClickListener {
            startActivity(Intent(this, ViewViolationsActivity::class.java))
        }

        btnPayFine.setOnClickListener {
            startActivity(Intent(this, PayFineActivity::class.java))
        }

        btnViolationHistory.setOnClickListener {
            startActivity(Intent(this, ViolationHistoryActivity::class.java))
        }

        bottomNav.selectedItemId = R.id.nav_dashboard

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, UserProfile::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}
