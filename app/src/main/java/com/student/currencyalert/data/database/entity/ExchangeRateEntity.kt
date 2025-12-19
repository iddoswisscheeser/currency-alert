package com.student.currencyalert.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey val currencyPair: String,
    val rate: Double,
    val timestamp: Long = System.currentTimeMillis()
)
