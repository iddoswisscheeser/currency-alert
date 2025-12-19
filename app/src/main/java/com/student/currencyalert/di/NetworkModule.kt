package com.student.currencyalert.di

import com.student.currencyalert.data.api.CurrencyApiService
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
    @Named("currency")
    fun provideCurrencyRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://currencyapi.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    @Provides
    @Singleton
    fun provideCurrencyApiService(@Named("currency") retrofit: Retrofit): CurrencyApiService =
        retrofit.create(CurrencyApiService::class.java)
    
    @Provides
    @Singleton
    @Named("frankfurter")
    fun provideFrankfurterRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.frankfurter.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    @Provides
    @Singleton
    fun provideFrankfurterService(@Named("frankfurter") retrofit: Retrofit): FrankfurterService =
        retrofit.create(FrankfurterService::class.java)
}
