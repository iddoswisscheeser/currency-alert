package com.student.currencyalert

import android.app.Application
import com.student.currencyalert.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CurrencyAlertApplication : Application() {
    
    @Inject
    lateinit var notificationHelper: NotificationHelper
    
    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel(this)
    }
}
