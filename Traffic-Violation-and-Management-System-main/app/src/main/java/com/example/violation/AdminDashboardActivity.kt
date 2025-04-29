package com.example.violation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val btnApplyFine = findViewById<Button>(R.id.btnApplyFine)
//        val btnReportViolations = findViewById<Button>(R.id.btnReportViolations)



        btnApplyFine.setOnClickListener {
            val intent = Intent(this, ApplyFineActivity::class.java)
            startActivity(intent)
        }
//
//        btnReportViolations.setOnClickListener {
//            Toast.makeText(this, "Violation Reported", Toast.LENGTH_SHORT).show()
//        }
    }
}