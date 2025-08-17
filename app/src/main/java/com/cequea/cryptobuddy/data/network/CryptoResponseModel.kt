package com.cequea.cryptobuddy.data.network
import com.cequea.cryptobuddy.domain.model.Crypto
import com.cequea.cryptobuddy.domain.model.Quote
import com.cequea.cryptobuddy.domain.model.USD
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CryptoResponseModel(
    val data: List<CryptoDataResponse>,
    val status: StatusResponse
)

data class CryptoDataResponse(
    @SerializedName("circulating_supply") val circulatingSupply: Double,
    @SerializedName("cmc_rank") val cmcRank: Int,
    @SerializedName("date_added") val dateAdded: String,
    @SerializedName("id") val id: Int,
    @SerializedName("infinite_supply") val infiniteSupply: Boolean,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("max_supply") val maxSupply: Double,
    @SerializedName("name") val name: String,
    @SerializedName("num_market_pairs") val numMarketPairs: Int,
    @SerializedName("platform") val platform: Any,
    @SerializedName("quote") val quote: QuoteResponse,
    @SerializedName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Any,
    @SerializedName("self_reported_market_cap") val selfReportedMarketCap: Any,
    @SerializedName("slug") val slug: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("total_supply") val totalSupply: Double,
    @SerializedName("tvl_ratio") val tvlRatio: Any
)

data class QuoteResponse(
    @SerializedName("USD") val usd: USDResponse
)

data class StatusResponse(
    @SerializedName("credit_count") val creditCount: Int,
    @SerializedName("elapsed") val elapsed: Int,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: Any,
    @SerializedName("notice") val notice: Any,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("total_count") val totalCount: Int
)

data class USDResponse(
    @SerializedName("fully_diluted_market_cap") val fullyDilutedMarketCap: Double,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("market_cap") val marketCap: Double,
    @SerializedName("market_cap_dominance") val marketCapDominance: Double,
    @SerializedName("percent_change_1h") val percentChange1h: Double,
    @SerializedName("percent_change_24h") val percentChange24h: Double,
    @SerializedName("percent_change_30d") val percentChange30d: Double,
    @SerializedName("percent_change_60d") val percentChange60d: Double,
    @SerializedName("percent_change_7d") val percentChange7d: Double,
    @SerializedName("percent_change_90d") val percentChange90d: Double,
    @SerializedName("price") val price: Double,
    @SerializedName("tvl") val tvl: Any,
    @SerializedName("volume_24h") val volume24h: Double,
    @SerializedName("volume_change_24h") val volumeChange24h: Double
)

private val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
fun CryptoDataResponse.toDomain(): Crypto =
    Crypto(
        id                = id,
        name              = name,
        symbol            = symbol,
        slug              = slug,
        creationDate      = LocalDateTime.parse(dateAdded, isoFormatter),
        maxSupply         = maxSupply,
        circulatingSupply = circulatingSupply,
        totalSupply       = totalSupply,
        lastUpdated       = LocalDateTime.parse(lastUpdated, isoFormatter),
        quote             = Quote(
            USD = USD(
                fullyDilutedMarketCap = quote.usd.fullyDilutedMarketCap,
                lastUpdated = quote.usd.lastUpdated,
                marketCap = quote.usd.marketCap,
                marketCapDominance = quote.usd.marketCapDominance,
                percentChange1h = quote.usd.percentChange1h,
                percentChange24h = quote.usd.percentChange24h,
                percentChange30d = quote.usd.percentChange30d,
                percentChange60d = quote.usd.percentChange60d,
                percentChange7d = quote.usd.percentChange7d,
                percentChange90d = quote.usd.percentChange90d,
                price = quote.usd.price,
                totalValueLocked = quote.usd.tvl,
                volume24h = quote.usd.volume24h,
                volumeChange24h = quote.usd.volumeChange24h
            )
        )
    )