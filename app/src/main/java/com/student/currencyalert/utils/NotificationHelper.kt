package com.student.currencyalert.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.student.currencyalert.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor() {
    
    companion object {
        private const val CHANNEL_ID = "currency_alerts"
        private const val NOTIFICATION_ID = 1
    }
    
    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Currency Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Exchange rate notifications"
        }
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    
    fun showRateNotification(context: Context, currency: String, rate: Double) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Exchange Rate Update")
            .setContentText("$currency: $rate")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
