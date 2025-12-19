package com.student.currencyalert.data.repository

import com.student.currencyalert.BuildConfig
import com.student.currencyalert.data.api.CurrencyApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepository @Inject constructor(
    private val currencyApiService: CurrencyApiService
) {
    suspend fun getExchangeRates(): Result<Double> {
        return try {
            val response = currencyApiService.getRates(BuildConfig.API_KEY)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val cadRate = body.rates["CAD"]
                    val krwRate = body.rates["KRW"]
                    
                    if (cadRate != null && krwRate != null) {
                        // Calculate KRW per CAD
                        val cadToKrw = krwRate / cadRate
                        Result.success(cadToKrw)
                    } else {
                        Result.failure(Exception("Currency rates not available"))
                    }
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
