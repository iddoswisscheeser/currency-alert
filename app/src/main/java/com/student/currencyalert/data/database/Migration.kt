package com.student.currencyalert.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE exchange_rates_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                currencyPair TEXT NOT NULL,
                rate REAL NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """)
        
        database.execSQL("""
            INSERT INTO exchange_rates_new (currencyPair, rate, timestamp)
            SELECT currencyPair, rate, timestamp FROM exchange_rates
        """)
        
        database.execSQL("DROP TABLE exchange_rates")
        
        database.execSQL("ALTER TABLE exchange_rates_new RENAME TO exchange_rates")
        
        database.execSQL("""
            CREATE INDEX index_exchange_rates_currencyPair_timestamp 
            ON exchange_rates(currencyPair, timestamp)
        """)
    }
}
