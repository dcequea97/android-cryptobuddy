package com.cequea.cryptobuddy.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.core.DataStore

private const val CRYPTO_PREFS = "crypto_prefs"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = CRYPTO_PREFS)

object CryptoPrefsKeys {
    val LAST_FETCH_TIME = longPreferencesKey("last_fetch_time_millis")
}
