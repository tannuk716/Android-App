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

class PayFineActivity : AppCompatActivity() {

    private lateinit var etFineId: EditText
    private lateinit var etAmount: EditText
    private lateinit var btnPay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_fine)

        etFineId = findViewById(R.id.etFineId)
        etAmount = findViewById(R.id.etAmount)
        btnPay = findViewById(R.id.btnPay)

        btnPay.setOnClickListener {
            val fineId = etFineId.text.toString().trim()
            val enteredAmount = etAmount.text.toString().trim()

            if (fineId.isNotEmpty() && enteredAmount.isNotEmpty()) {
                payFine(fineId, enteredAmount)
            } else {
                FancyToast.makeText(this, "Please fill all fields", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show()
            }
        }
    }

    private fun payFine(fineId: String, enteredAmount: String) {
        val userId = SessionManager(this).getUserId()
        if (userId.isEmpty()) {
            FancyToast.makeText(this, "User not logged in", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show()
            return
        }

        val sharedPref = getSharedPreferences("FINE_DATA_$userId", Context.MODE_PRIVATE)
        val gson = Gson()
        val finesJson = sharedPref.getString("fines", "[]")
        val type = object : TypeToken<MutableList<MutableMap<String, Any>>>() {}.type
        val fines: MutableList<MutableMap<String, Any>> = gson.fromJson(finesJson, type)

        val fine = fines.find { it["fineId"] == fineId && it["paid"] == false }

        if (fine != null) {
            val fineAmount = (fine["amount"] as? String)?.toDoubleOrNull()
            val entered = enteredAmount.toDoubleOrNull()

            if (fineAmount != null && entered != null && fineAmount == entered) {
                fine["paid"] = true

                with(sharedPref.edit()) {
                    putString("fines", gson.toJson(fines))
                    apply()
                }

                FancyToast.makeText(this, "Fine $fineId paid successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
                finish()
            } else {
                FancyToast.makeText(this, "Incorrect amount entered", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
            }
        } else {
            FancyToast.makeText(this, "Fine ID not found or already paid", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
        }
    }
}
