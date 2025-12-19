package com.student.currencyalert.di

import com.student.currencyalert.data.api.ExchangeRateService
import com.student.currencyalert.data.api.FrankfurterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://currencyapi.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    @Provides
    @Singleton
    fun provideExchangeRateService(retrofit: Retrofit): ExchangeRateService =
        retrofit.create(ExchangeRateService::class.java)
    
    @Provides
    @Singleton
    fun provideFrankfurterRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.frankfurter.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    @Provides
    @Singleton
    fun provideFrankfurterService(@Named("frankfurter") retrofit: Retrofit): FrankfurterService =
        retrofit.create(FrankfurterService::class.java)
    
    @Provides
    @Singleton
    @Named("frankfurter")
    fun provideFrankfurterRetrofitNamed(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.frankfurter.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
