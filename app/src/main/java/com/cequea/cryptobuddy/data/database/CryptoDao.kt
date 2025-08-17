package com.cequea.cryptobuddy.data.database

import androidx.room.*
import com.cequea.cryptobuddy.data.database.model.CryptoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {

    @Query("SELECT * FROM crypto_entity")
    fun getAllCryptosFlow(): Flow<List<CryptoEntity>>

    @Query("SELECT * FROM crypto_entity")
    suspend fun getAllCryptos(): List<CryptoEntity>

    @Query("SELECT * FROM crypto_entity WHERE id = :id LIMIT 1")
    suspend fun getCryptoById(id: Int): CryptoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrypto(crypto: CryptoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cryptos: List<CryptoEntity>)

    @Update
    suspend fun updateCrypto(crypto: CryptoEntity)

    @Delete
    suspend fun deleteCrypto(crypto: CryptoEntity)
}