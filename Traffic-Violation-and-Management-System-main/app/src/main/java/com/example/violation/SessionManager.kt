package com.example.violation

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_FULL_NAME = "full_name" }

    // Save user ID
    fun saveUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    // Get user ID
    fun getUserId(): String {
        return prefs.getString(KEY_USER_ID, "") ?: ""
    }

    // Save full name
    fun saveFullName(fullName: String) {
        prefs.edit().putString(KEY_FULL_NAME, fullName).apply()
    }

    // Get full name
    fun getFullName(): String {
        return prefs.getString(KEY_FULL_NAME, "") ?: "Unknown"  // Default to "Unknown" if not found
    }

    // Save auth token and user role
    fun saveAuthToken(token: String, role: String) {
        prefs.edit().apply {
            putString(KEY_AUTH_TOKEN, token)
            putString(KEY_USER_ROLE, role)
            apply()
        }
    }

    // Get auth token
    fun getAuthToken(): String {
        return prefs.getString(KEY_AUTH_TOKEN, "") ?: ""
    }

    // Get user role
    fun getUserRole(): String {
        return prefs.getString(KEY_USER_ROLE, "") ?: ""
    }

    // Clear session
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
