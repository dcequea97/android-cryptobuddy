package com.cequea.cryptobuddy.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cequea.cryptobuddy.ui.screens.cryptoDetail.CryptoDetailRoot
import com.cequea.cryptobuddy.ui.screens.cryptoList.CryptoListRoot

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {},
        bottomBar = {},
        content = { paddingValues ->
            NavHost(
                modifier = Modifier
                    .padding(paddingValues),
                navController = navController,
                startDestination = AppScreens.CryptoListScreen
            ) {
                composable<AppScreens.CryptoListScreen> {
                    CryptoListRoot(navController = navController)
                }

                composable<AppScreens.CryptoDetailScreen> { backStackEntry ->
                    val args = backStackEntry.toRoute<AppScreens.CryptoDetailScreen>()
                    CryptoDetailRoot(args.cryptoId, navController)
                }
            }
        }
    )
}