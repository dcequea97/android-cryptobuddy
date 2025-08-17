package com.cequea.cryptobuddy.ui.screens.cryptoList

sealed interface CryptoListAction {
    data class OnSearchTextChanged(val text: String): CryptoListAction
    data class OnCryptoClicked(val cryptoId: Int): CryptoListAction
    data object OnRefresh: CryptoListAction
}
