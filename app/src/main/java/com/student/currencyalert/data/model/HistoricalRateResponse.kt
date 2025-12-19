package com.student.currencyalert.data.model

import com.google.gson.annotations.SerializedName

data class HistoricalRateResponse(
    val amount: Double,
    val base: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val rates: Map<String, Map<String, Double>>
)
