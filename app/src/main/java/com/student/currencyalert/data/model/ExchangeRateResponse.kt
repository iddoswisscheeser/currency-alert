package com.student.currencyalert.data.model

data class ExchangeRateResponse(
    val amount: Double = 1.0,
    val base: String,
    val date: String? = null,
    val rates: Map<String, Double>
)
