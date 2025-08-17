package com.cequea.cryptobuddy.data.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.cequea.cryptobuddy.data.database.CryptoDao
import com.cequea.cryptobuddy.data.database.model.toDomain
import com.cequea.cryptobuddy.data.database.model.toEntity
import com.cequea.cryptobuddy.domain.CryptoRepository
import com.cequea.cryptobuddy.domain.TimeProvider
import com.cequea.cryptobuddy.domain.model.Crypto
import com.cequea.cryptobuddy.utils.CryptoPrefsKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CryptoRepositoryImpl(
    private val api: CryptoApi,
    private val timeProvider: TimeProvider,
    private val cryptoDao: CryptoDao,
    private val dataStore: DataStore<Preferences>
) : CryptoRepository {
    private var lastFetchTimeMillis: Long = 0L
    private val throttleInterval = 5 * 60 * 1_000L

    override fun getCryptoFlow(): Flow<List<Crypto>> {
        return cryptoDao.getAllCryptosFlow()
            .map { data ->
                data.map { cryptoEntity -> cryptoEntity.toDomain() }
            }
    }

    override suspend fun getAllCryptos() {
        val now = timeProvider.nowMillis()

        val lastFetch = dataStore.data
            .map { prefs -> prefs[CryptoPrefsKeys.LAST_FETCH_TIME] ?: 0L }
            .first()

        dataStore.edit { prefs ->
            prefs[CryptoPrefsKeys.LAST_FETCH_TIME] = now
        }

        if (now - lastFetch < throttleInterval) {
            return
        }

        try {
            val response = api.getAllCryptos()
            val result = response.data.map { it.toDomain() }
            lastFetchTimeMillis = now
            val entities = result.map { it.toEntity() }
            cryptoDao.insertAll(entities)

            result
        } catch (e: Exception) {

        }
    }

    override suspend fun getCryptoById(id: Int): Crypto? {
        return cryptoDao.getCryptoById(id)?.toDomain()
    }
}