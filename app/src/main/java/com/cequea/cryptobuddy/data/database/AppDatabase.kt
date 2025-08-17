package com.cequea.cryptobuddy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cequea.cryptobuddy.data.database.model.CryptoEntity

@Database(
    entities = [
        CryptoEntity::class,
//        QuoteEntity::class,
//        UsdEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao
}