package com.student.currencyalert.data.api

import com.student.currencyalert.data.model.HistoricalRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FrankfurterService {
    @GET("v1/{date_range}")
    suspend fun getHistoricalRates(
        @Path("date_range") dateRange: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") symbols: String
    ): Response<HistoricalRateResponse>
}
