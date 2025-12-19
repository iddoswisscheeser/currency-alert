package com.student.currencyalert.data.api

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("time_last_update_unix")
    val timestamp: Long,
    @SerializedName("conversion_rate")
    val rate: Double
)
