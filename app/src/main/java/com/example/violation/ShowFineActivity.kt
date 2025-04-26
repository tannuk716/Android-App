package com.example.violation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShowFineActivity : AppCompatActivity() {

    private lateinit var fineListView: ListView
    private lateinit var fineList: MutableList<Map<String, Any>>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_fine)

        fineListView = findViewById(R.id.fineListView)

        val sharedPref = getSharedPreferences("ALL_FINES", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("fines", "[]")
        val type = object : TypeToken<MutableList<Map<String, Any>>>() {}.type
        fineList = gson.fromJson(json, type)

        val fineStrings = fineList.map {
            "Fine ID: ${it["fineId"]}\nUser ID: ${it["userId"]}\nViolation: ${it["violation"]}\nAmount: ₹${it["amount"]}\n" +
                    "Bike No: ${it["bikeNo"]}\nType: ${it["violationType"]}\nDate: ${it["date"]}\n" +
                    "Paid: ${if (it["paid"] as Boolean) "Yes" else "No"}\n" +
                    "Payment Date: ${if ((it["paymentDate"] as Double).toLong() == 0L) "N/A" else it["paymentDate"]}"
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fineStrings)
        fineListView.adapter = adapter

        fineListView.setOnItemLongClickListener { _, _, position, _ ->
            val fineIdToDelete = fineList[position]["fineId"].toString()

            AlertDialog.Builder(this)
                .setTitle("Delete Fine")
                .setMessage("Are you sure you want to delete Fine ID: $fineIdToDelete?")
                .setPositiveButton("Delete") { _, _ ->
                    fineList.removeAt(position)
                    sharedPref.edit().putString("fines", gson.toJson(fineList)).apply()
                    recreate() // Refresh UI
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }
    }
}
