package com.student.currencyalert.data.api

import com.student.currencyalert.data.model.CurrencyApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("v1/rates")
    suspend fun getRates(
        @Query("key") apiKey: String,
        @Query("output") output: String = "json"
    ): Response<CurrencyApiResponse>
}
