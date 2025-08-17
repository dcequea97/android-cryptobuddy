package com.cequea.cryptobuddy.data.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cequea.cryptobuddy.utils.LocalDateTimeConverter
import java.time.LocalDateTime

@Entity(tableName = "crypto_entity")
@TypeConverters(LocalDateTimeConverter::class)
data class CryptoEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,

    val creationDate: LocalDateTime,
    val maxSupply: Double,
    val circulatingSupply: Double,
    val totalSupply: Double,
    val lastUpdated: LocalDateTime,

    @Embedded val quoteEntity: QuoteEntity
)