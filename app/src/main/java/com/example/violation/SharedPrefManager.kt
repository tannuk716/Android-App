package com.example.violation

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefManager private constructor(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    companion object {
        private const val PREF_NAME = "AppPrefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PASSWORD = "user_password"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ROLE = "userRole"
        private const val KEY_AUTH_TOKEN = "authToken"
        private const val KEY_FINE_DATA = "fineData"

        // Admin credentials
        private const val ADMIN_EMAIL = "vikas2412@gmail.com"
        private const val ADMIN_PASSWORD = "Vik@s2412"

        @Volatile
        private var instance: SharedPrefManager? = null

        fun getInstance(context: Context): SharedPrefManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPrefManager(context).also { instance = it }
            }
        }
    }

    // User ID management
    fun saveUserId(userId: String) {
        editor.putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String {
        return sharedPref.getString(KEY_USER_ID, "") ?: ""
    }

    // User credentials
    fun saveUserCredentials(username: String, email: String) {
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_USER_EMAIL, email)
        editor.apply()
    }

    fun getUserCredentials(): Pair<String, String>? {
        val username = sharedPref.getString(KEY_USERNAME, null)
        val email = sharedPref.getString(KEY_USER_EMAIL, null)
        return if (username != null && email != null) Pair(username, email) else null
    }

    // Password management
    fun saveUserPassword(password: String) {
        editor.putString(KEY_USER_PASSWORD, password).apply()
    }

    fun getUserPassword(): String? {
        return sharedPref.getString(KEY_USER_PASSWORD, null)
    }

    // Session management
    fun saveSession(username: String, role: String, token: String) {
        editor.apply {
            putString(KEY_USERNAME, username)
            putString(KEY_USER_ROLE, role)
            putString(KEY_AUTH_TOKEN, token)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserRole(): String? {
        return sharedPref.getString(KEY_USER_ROLE, null)
    }

    fun clearSession() {
        editor.clear().apply()
    }

    // Admin functions
    fun isValidAdmin(email: String, password: String): Boolean {
        return email == ADMIN_EMAIL && password == ADMIN_PASSWORD
    }

    // Fine Data management
    data class Fine(
        val fineId: String,
        val violation: String,
        val amount: Double,
        val date: String,
        var paid: Boolean = false,
        var paymentDate: Long? = null
    )

    fun saveFineData(fines: List<Fine>) {
        val finesJson = Gson().toJson(fines)
        editor.putString(KEY_FINE_DATA, finesJson).apply()
    }

    fun getFineData(): List<Fine> {
        val finesJson = sharedPref.getString(KEY_FINE_DATA, "[]") ?: "[]"
        val type = object : TypeToken<List<Fine>>() {}.type
        return Gson().fromJson(finesJson, type)
    }

    fun updateFine(updatedFine: Fine) {
        val fines = getFineData().toMutableList()
        val index = fines.indexOfFirst { it.fineId == updatedFine.fineId }
        if (index != -1) {
            fines[index] = updatedFine
            saveFineData(fines)
        }
    }

    // Email verification
    fun doesEmailExist(email: String): Boolean {
        val storedEmail = sharedPref.getString(KEY_USER_EMAIL, null)
        return storedEmail == email
    }

    // Profile display methods
    fun getDisplayName(): String {
        return sharedPref.getString(KEY_USERNAME, "Guest User") ?: "Guest User"
    }

    fun getDisplayEmail(): String {
        return sharedPref.getString(KEY_USER_EMAIL, "no-email@example.com") ?: "no-email@example.com"
    }

    fun getProfileDisplayData(): Pair<String, String> {
        return Pair(getDisplayName(), getDisplayEmail())
    }
}