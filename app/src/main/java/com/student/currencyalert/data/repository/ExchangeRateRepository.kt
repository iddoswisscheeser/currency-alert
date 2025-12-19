package com.student.currencyalert.data.repository

import com.student.currencyalert.BuildConfig
import com.student.currencyalert.data.api.ExchangeRateService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepository @Inject constructor(
    private val apiService: ExchangeRateService
) {
    suspend fun getExchangeRates(baseCurrency: String): Result<Map<String, Double>> {
        return try {
            val response = apiService.getExchangeRates(BuildConfig.API_KEY, baseCurrency)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it.rates)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
