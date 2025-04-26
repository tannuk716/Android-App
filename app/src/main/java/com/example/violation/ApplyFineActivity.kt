package com.example.violation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.sony.fancytoastlib.FancyToast
import java.text.SimpleDateFormat
import java.util.*

class ApplyFineActivity : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var violationDetailsEditText: EditText
    private lateinit var fineAmountEditText: EditText
    private lateinit var bikeNoEditText: EditText
    private lateinit var violationTypeSpinner: Spinner
    private lateinit var applyFineButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_fine)

        userIdEditText = findViewById(R.id.userIdEditText)
        violationDetailsEditText = findViewById(R.id.violationDetailsEditText)
        fineAmountEditText = findViewById(R.id.fineAmountEditText)
        bikeNoEditText = findViewById(R.id.bikeNoEditText)
        violationTypeSpinner = findViewById(R.id.violationTypeSpinner)
        applyFineButton = findViewById(R.id.applyFineButton)

        setupViolationTypeSpinner()

        applyFineButton.setOnClickListener {
            val userId = userIdEditText.text.toString().trim()
            val violation = violationDetailsEditText.text.toString().trim()
            val fineAmount = fineAmountEditText.text.toString().trim()
            val bikeNo = bikeNoEditText.text.toString().trim()
            val violationType = violationTypeSpinner.selectedItem.toString()

            if (userId.isNotEmpty() && violation.isNotEmpty() && fineAmount.isNotEmpty() && bikeNo.isNotEmpty() && violationType != "Select Violation Type") {
                saveFineData(userId, violation, fineAmount, bikeNo, violationType)
            } else {
                FancyToast.makeText(this, "Please fill all fields", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show()
            }
        }
    }

    private fun setupViolationTypeSpinner() {
        val violationTypes = arrayOf(
            "Select Violation Type",
            "Speeding",
            "Parking Violation",
            "Red Light Violation",
            "No Helmet",
            "Document Expired"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, violationTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        violationTypeSpinner.adapter = adapter
    }

    private fun saveFineData(userId: String, violation: String, fineAmount: String, bikeNo: String, violationType: String) {
        val sharedPref = getSharedPreferences("ALL_FINES", Context.MODE_PRIVATE)
        val gson = Gson()
        val existingJson = sharedPref.getString("fines", "[]")
        val type = object : TypeToken<MutableList<Map<String, Any>>>() {}.type
        val fines: MutableList<Map<String, Any>> = gson.fromJson(existingJson, type)

        val fineId = "FINE" + System.currentTimeMillis().toString().takeLast(6)
        val fineDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        val fine = mapOf(
            "fineId" to fineId,
            "userId" to userId,
            "violation" to violation,
            "amount" to fineAmount,
            "bikeNo" to bikeNo,
            "violationType" to violationType,
            "date" to fineDate,
            "paid" to false,
            "paymentDate" to 0L
        )

        fines.add(fine)
        with(sharedPref.edit()) {
            putString("fines", gson.toJson(fines))
            apply()
        }

        startActivity(Intent(this, ShowFineActivity::class.java))
        FancyToast.makeText(this, "Fine applied!\nFine ID: $fineId", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
        finish()
    }
}
