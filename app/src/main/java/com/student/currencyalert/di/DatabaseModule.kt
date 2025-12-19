package com.student.currencyalert.di

import android.content.Context
import com.student.currencyalert.data.database.CurrencyDatabase
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
        CurrencyDatabase.create(context)
    
    @Provides
    fun provideExchangeRateDao(database: CurrencyDatabase): ExchangeRateDao =
        database.exchangeRateDao()
}
