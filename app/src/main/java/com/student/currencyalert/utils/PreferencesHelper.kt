package com.student.currencyalert.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("currency_settings", Context.MODE_PRIVATE)
    
    fun getCadence(): Int = prefs.getInt("cadence_hours", 1)
    
    fun getNotificationsEnabled(): Boolean = prefs.getBoolean("notifications_enabled", true)
    
    fun saveCadence(hours: Int) {
        prefs.edit().putInt("cadence_hours", hours).apply()
    }
    
    fun saveNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }
}
