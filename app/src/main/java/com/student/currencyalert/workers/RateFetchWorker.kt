package com.student.currencyalert.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.student.currencyalert.data.database.dao.ExchangeRateDao
import com.student.currencyalert.data.database.entity.ExchangeRateEntity
import com.student.currencyalert.data.repository.ExchangeRateRepository
import com.student.currencyalert.utils.NotificationHelper
import com.student.currencyalert.utils.PreferencesHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalTime
import java.util.concurrent.TimeUnit

@HiltWorker
class RateFetchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ExchangeRateRepository,
    private val notificationHelper: NotificationHelper,
    private val preferencesHelper: PreferencesHelper,
    private val exchangeRateDao: ExchangeRateDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            if (!preferencesHelper.getNotificationsEnabled()) {
                return Result.success()
            }
            
            val result = repository.getExchangeRates()
            val currentRate = result.getOrNull() ?: return Result.retry()
            
            exchangeRateDao.insertRates(listOf(
                ExchangeRateEntity(
                    currencyPair = "CAD_KRW",
                    rate = currentRate,
                    timestamp = System.currentTimeMillis()
                )
            ))
            
            val cadence = preferencesHelper.getCadence()
            val lastNotificationTime = preferencesHelper.getPreviousRate()?.let { 
                System.currentTimeMillis() 
            } ?: 0L
            
            val hoursSinceLastNotification = (System.currentTimeMillis() - lastNotificationTime) / (1000 * 60 * 60)
            
            if (hoursSinceLastNotification >= cadence) {
                if (isWithinNotificationWindow()) {
                    notificationHelper.showRateNotification(applicationContext, currentRate)
                    preferencesHelper.savePreviousRate(currentRate)
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    private fun isWithinNotificationWindow(): Boolean {
        val now = LocalTime.now()
        val start = LocalTime.parse(preferencesHelper.getStartTime())
        val end = LocalTime.parse(preferencesHelper.getEndTime())
        
        return if (end.isAfter(start)) {
            now.isAfter(start) && now.isBefore(end)
        } else {
            now.isAfter(start) || now.isBefore(end)
        }
    }

    companion object {
        fun scheduleWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<RateFetchWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "rate_fetch_work",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}
