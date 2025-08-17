package com.cequea.cryptobuddy.domain

import com.cequea.cryptobuddy.domain.model.Crypto
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getCryptoFlow(): Flow<List<Crypto>>

    suspend fun getCryptoById(id: Int): Crypto?

    suspend fun getAllCryptos()
}