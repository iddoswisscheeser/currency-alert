package com.student.currencyalert.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exchange_rates",
    indices = [Index(value = ["currencyPair", "timestamp"])]
)
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val currencyPair: String,
    val rate: Double,
    val timestamp: Long = System.currentTimeMillis()
)
