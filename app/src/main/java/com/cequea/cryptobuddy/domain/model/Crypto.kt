package com.cequea.cryptobuddy.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Crypto(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val creationDate: LocalDateTime,
    val maxSupply: Double,
    val circulatingSupply: Double,
    val totalSupply: Double,
    val lastUpdated: LocalDateTime,
    val quote: Quote
)

data class Quote(
    val USD: USD
)

data class USD(
    val fullyDilutedMarketCap: Double,
    val lastUpdated: String,
    val marketCap: Double,
    val marketCapDominance: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange30d: Double,
    val percentChange60d: Double,
    val percentChange7d: Double,
    val percentChange90d: Double,
    val price: Double,
    val totalValueLocked: Any?, // renamed tvl
    val volume24h: Double,
    val volumeChange24h: Double
)

fun getDummyCrypto(): Crypto {
    val usd = USD(
        fullyDilutedMarketCap = 2391347927807.53,
        lastUpdated = "2025-08-06T01:16:00.000Z",
        marketCap = 2266367544571.9995,
        marketCapDominance = 61.1717,
        percentChange1h = -0.1215237,
        percentChange24h = -0.90759362,
        percentChange30d = 4.67850002,
        percentChange60d = 8.93177883,
        percentChange7d = -3.18826472,
        percentChange90d = 16.43639341,
        price = 113873.71084797784,
        totalValueLocked = null,
        volume24h = 61040556833.14668,
        volumeChange24h = 13.0871
    )

    val quote = Quote(USD = usd)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    return Crypto(
        id = 1,
        name = "Bitcoin",
        symbol = "BTC",
        slug = "bitcoin",
        creationDate = LocalDateTime.parse("2010-07-13T00:00:00.000Z", formatter),
        maxSupply = 21000000.0,
        circulatingSupply = 19902465.0,
        totalSupply = 19902465.0,
        lastUpdated = LocalDateTime.parse("2025-08-05T00:23:00.000Z", formatter),
        quote = quote
    )
}