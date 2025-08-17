package com.cequea.cryptobuddy.data.database.model

import androidx.room.Embedded

data class QuoteEntity(
    @Embedded(prefix = "usd_") val usdEntity: UsdEntity
)