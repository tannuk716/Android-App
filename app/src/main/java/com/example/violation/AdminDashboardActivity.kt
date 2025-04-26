package com.example.violation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val btnApplyFine = findViewById<Button>(R.id.btnApplyFine)
        val btnViewViolations = findViewById<Button>(R.id.btnViewViolations)

        btnApplyFine.setOnClickListener {
            startActivity(Intent(this, ApplyFineActivity::class.java))
        }

        btnViewViolations.setOnClickListener {
            startActivity(Intent(this, ShowFineActivity::class.java))



        }
    }
}