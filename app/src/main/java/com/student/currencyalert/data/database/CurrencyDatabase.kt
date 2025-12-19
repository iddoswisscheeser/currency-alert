package com.student.currencyalert.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.student.currencyalert.data.database.dao.ExchangeRateDao
import com.student.currencyalert.data.database.entity.ExchangeRateEntity

@Database(
    entities = [ExchangeRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
    
    companion object {
        fun create(context: Context): CurrencyDatabase {
            return Room.databaseBuilder(
                context,
                CurrencyDatabase::class.java,
                "currency_database"
            ).build()
        }
    }
}
