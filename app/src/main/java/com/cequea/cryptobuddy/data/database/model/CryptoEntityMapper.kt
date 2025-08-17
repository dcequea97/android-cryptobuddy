package com.cequea.cryptobuddy.data.database.model

import com.cequea.cryptobuddy.domain.model.Crypto
import com.cequea.cryptobuddy.domain.model.Quote
import com.cequea.cryptobuddy.domain.model.USD

fun CryptoEntity.toDomain(): Crypto = Crypto(
    id = id,
    name = name,
    symbol = symbol,
    slug = slug,
    creationDate = creationDate,
    maxSupply = maxSupply,
    circulatingSupply = circulatingSupply,
    totalSupply = totalSupply,
    lastUpdated = lastUpdated,
    quote = quoteEntity.toDomain()
)

private fun QuoteEntity.toDomain(): Quote =
    Quote(
        USD = usdEntity.toDomain()
    )

private fun UsdEntity.toDomain(): USD =
    USD(
        fullyDilutedMarketCap = fullyDilutedMarketCap,
        lastUpdated = lastUpdated,
        marketCap = marketCap,
        marketCapDominance = marketCapDominance,
        percentChange1h = percentChange1h,
        percentChange24h = percentChange24h,
        percentChange30d = percentChange30d,
        percentChange60d = percentChange60d,
        percentChange7d = percentChange7d,
        percentChange90d = percentChange90d,
        price = price,
        totalValueLocked = totalValueLocked,
        volume24h = volume24h,
        volumeChange24h = volumeChange24h
    )


fun Crypto.toEntity(): CryptoEntity = CryptoEntity(
    id                = id,
    name              = name,
    symbol            = symbol,
    slug              = slug,
    creationDate      = creationDate,
    maxSupply         = maxSupply,
    circulatingSupply = circulatingSupply,
    totalSupply       = totalSupply,
    lastUpdated       = lastUpdated,
    quoteEntity       = quote.toEntity()
)

private fun Quote.toEntity(): QuoteEntity =
    QuoteEntity(
        usdEntity = USD.toEntity()
    )

private fun USD.toEntity(): UsdEntity =
    UsdEntity(
        fullyDilutedMarketCap = fullyDilutedMarketCap,
        lastUpdated            = lastUpdated,
        marketCap              = marketCap,
        marketCapDominance     = marketCapDominance,
        percentChange1h        = percentChange1h,
        percentChange24h       = percentChange24h,
        percentChange7d        = percentChange7d,
        percentChange30d       = percentChange30d,
        percentChange60d       = percentChange60d,
        percentChange90d       = percentChange90d,
        price                  = price,
        totalValueLocked       = totalValueLocked as Double?,
        volume24h              = volume24h,
        volumeChange24h        = volumeChange24h
    )