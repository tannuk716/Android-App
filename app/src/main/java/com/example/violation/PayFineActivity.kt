package com.example.violation

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.sony.fancytoastlib.FancyToast

class PayFineActivity : AppCompatActivity() {

    private lateinit var etFineId: EditText
    private lateinit var etAmount: EditText
    private lateinit var btnPay: Button
    private lateinit var spViolationType: Spinner
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_fine)

        // Initialize SharedPrefManager
        sharedPrefManager = SharedPrefManager.getInstance(this)

        // Initialize views
        etFineId = findViewById(R.id.etFineId)
        etAmount = findViewById(R.id.etAmount)
        btnPay = findViewById(R.id.btnPay)
        spViolationType = findViewById(R.id.spViolationType)

        // Setup violation type spinner
        setupViolationSpinner()

        btnPay.setOnClickListener {
            val fineId = etFineId.text.toString().trim()
            val enteredAmount = etAmount.text.toString().trim()

            when {
                fineId.isEmpty() -> {
                    etFineId.error = "Please enter fine ID"
                    etFineId.requestFocus()
                }
                enteredAmount.isEmpty() -> {
                    etAmount.error = "Please enter amount"
                    etAmount.requestFocus()
                }
                else -> processPayment(fineId, enteredAmount)
            }
        }
    }

    private fun setupViolationSpinner() {
        val violationTypes = arrayOf(
            "Select violation type",
            "Speeding",
            "Parking Violation",
            "Red Light Violation",
            "No Helmet",
            "Document Expired"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            violationTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spViolationType.adapter = adapter
    }

    private fun processPayment(fineId: String, enteredAmount: String) {
        val userId = sharedPrefManager.getUserId()

        if (userId.isEmpty()) {
            showToast("User not logged in", FancyToast.WARNING)
            return
        }

        try {
            val amount = enteredAmount.toDouble()
            val fines = sharedPrefManager.getFineData()
            val fine = fines.find { it.fineId == fineId && !it.paid }

            when {
                fine == null -> showToast("Fine not found or already paid", FancyToast.WARNING)
                fine.amount != amount -> showToast("Amount doesn't match fine amount", FancyToast.WARNING)
                else -> {
                    // Update fine status
                    fine.paid = true
                    fine.paymentDate = System.currentTimeMillis()

                    // Save updated fines
                    sharedPrefManager.updateFine(fine)

                    showToast("Payment successful!", FancyToast.SUCCESS)
                    finish()
                }
            }
        } catch (e: NumberFormatException) {
            showToast("Invalid amount format", FancyToast.ERROR)
        }
    }

    private fun showToast(message: String, type: Int) {
        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, type, true).show()
    }
}
