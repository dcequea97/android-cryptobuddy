package com.cequea.cryptobuddy.ui.navigation

import kotlinx.serialization.Serializable

sealed class AppScreens : Screen {
    @Serializable
    data object CryptoListScreen: AppScreens()

    @Serializable
    data class CryptoDetailScreen(val cryptoId: Int): AppScreens()
}