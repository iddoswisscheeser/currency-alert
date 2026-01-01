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
    
    fun getStartTime(): String = prefs.getString("start_time", "09:00") ?: "09:00"
    
    fun getEndTime(): String = prefs.getString("end_time", "21:00") ?: "21:00"
    
    fun saveStartTime(time: String) {
        prefs.edit().putString("start_time", time).apply()
    }
    
    fun saveEndTime(time: String) {
        prefs.edit().putString("end_time", time).apply()
    }
    
    fun getPreviousRate(): Double? {
        val rate = prefs.getFloat("previous_rate", -1f)
        return if (rate == -1f) null else rate.toDouble()
    }
    
    fun savePreviousRate(rate: Double) {
        prefs.edit().putFloat("previous_rate", rate.toFloat()).apply()
    }
}
