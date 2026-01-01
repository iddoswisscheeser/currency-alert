package com.student.currencyalert.data.database.dao

import androidx.room.*
import com.student.currencyalert.data.database.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM exchange_rates")
    fun getAllRates(): Flow<List<ExchangeRateEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRateEntity>)
    
    @Query("DELETE FROM exchange_rates")
    suspend fun clearAll()
    
    @Query("""
        SELECT * FROM exchange_rates 
        WHERE currencyPair = :currencyPair 
        AND timestamp >= :startTime 
        ORDER BY timestamp ASC
    """)
    suspend fun getRatesInPeriod(currencyPair: String, startTime: Long): List<ExchangeRateEntity>
    
    @Query("SELECT * FROM exchange_rates WHERE currencyPair = :currencyPair ORDER BY timestamp DESC LIMIT 1")
    fun getLatestRate(currencyPair: String): Flow<ExchangeRateEntity?>
}
