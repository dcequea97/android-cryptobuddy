package com.cequea.cryptobuddy.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {
    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getAllCryptos(
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "market_cap",
        @Query("cryptocurrency_type") cryptocurrencyType: String = "all",
        @Query("tag") tag: String = "all"
//        @Query("limit") limit: Int = 30
    ): CryptoResponseModel
}