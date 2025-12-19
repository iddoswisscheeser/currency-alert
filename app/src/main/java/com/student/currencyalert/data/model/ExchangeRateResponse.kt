package com.student.currencyalert.data.model

data class ExchangeRateResponse(
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
