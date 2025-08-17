package com.cequea.cryptobuddy.ui.screens.cryptoList

import com.cequea.cryptobuddy.domain.model.Crypto

data class CryptoListState(
    val isLoading: Boolean = true,
    val cryptos: List<Crypto> = listOf(),
    val cryptosFiltered: List<Crypto> = listOf(),
    val searchBarText: String = "",
    val isRefreshing: Boolean = false
)