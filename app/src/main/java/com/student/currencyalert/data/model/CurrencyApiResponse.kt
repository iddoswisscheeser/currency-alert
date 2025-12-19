package com.student.currencyalert.data.model

data class CurrencyApiResponse(
    val valid: Boolean = true,
    val base: String = "USD",
    val rates: Map<String, Double>
)
