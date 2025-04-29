package com.example.violation

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.sony.fancytoastlib.FancyToast

class ApplyFineActivity : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var violationDetailsEditText: EditText
    private lateinit var fineAmountEditText: EditText
    private lateinit var applyFineButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_fine)

        userIdEditText = findViewById(R.id.userIdEditText)
        violationDetailsEditText = findViewById(R.id.violationDetailsEditText)
        fineAmountEditText = findViewById(R.id.fineAmountEditText)
        applyFineButton = findViewById(R.id.applyFineButton)

        applyFineButton.setOnClickListener {
            val userId = userIdEditText.text.toString().trim()
            val violation = violationDetailsEditText.text.toString().trim()
            val fineAmount = fineAmountEditText.text.toString().trim()

            if (userId.isNotEmpty() && violation.isNotEmpty() && fineAmount.isNotEmpty()) {
                saveFineData(userId, violation, fineAmount)
            } else {
                FancyToast.makeText(this, "Please fill all fields", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show()
            }
        }
    }

    private fun saveFineData(userId: String, violation: String, fineAmount: String) {
        val sharedPref = getSharedPreferences("FINE_DATA_$userId", Context.MODE_PRIVATE)
        val gson = Gson()
        val existingJson = sharedPref.getString("fines", "[]")
        val type = object : TypeToken<MutableList<MutableMap<String, Any>>>() {}.type
        val fines: MutableList<MutableMap<String, Any>> = gson.fromJson(existingJson, type)

        val fineId = "FINE" + System.currentTimeMillis().toString().takeLast(6)
        val fine = mutableMapOf<String, Any>(
            "fineId" to fineId,
            "violation" to violation,
            "amount" to fineAmount,
            "paid" to false
        )

        fines.add(fine)
        with(sharedPref.edit()) {
            putString("fines", gson.toJson(fines))
            apply()
        }

        FancyToast.makeText(this, "Fine applied!\nFine ID: $fineId", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
        finish()
    }
}
