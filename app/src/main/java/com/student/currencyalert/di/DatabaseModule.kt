package com.student.currencyalert.di

import android.content.Context
import androidx.room.Room
import com.student.currencyalert.data.database.CurrencyDatabase
import com.student.currencyalert.data.database.MIGRATION_1_2
import com.student.currencyalert.data.database.dao.ExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CurrencyDatabase =
        Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "currency_database"
        )
        .addMigrations(MIGRATION_1_2)
        .build()
    
    @Provides
    fun provideExchangeRateDao(database: CurrencyDatabase): ExchangeRateDao =
        database.exchangeRateDao()
}
