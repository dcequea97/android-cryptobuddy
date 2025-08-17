package com.cequea.cryptobuddy.di

import androidx.room.Room
import com.cequea.cryptobuddy.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


private const val DATABASE_NAME = "crypto_buddy_app"

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .build()
    }

    single { get<AppDatabase>().cryptoDao() }
}