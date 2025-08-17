package com.cequea.cryptobuddy.ui.screens.cryptoList

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.cequea.cryptobuddy.domain.model.getDummyCrypto
import com.cequea.cryptobuddy.ui.components.CryptoListItem
import com.cequea.cryptobuddy.ui.navigation.AppScreens
import com.cequea.cryptobuddy.ui.theme.CryptoBuddyTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CryptoListRoot(
    navController: NavController
) {
    val viewModel: CryptoListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.onNavigateToDetail.collectLatest { cryptoId ->
            navController.navigate(AppScreens.CryptoDetailScreen(cryptoId))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CryptoListScreen(
            state = state,
            onAction = viewModel::onAction
        )

        if (state.isLoading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListScreen(
    state: CryptoListState,
    onAction: (CryptoListAction) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            },
            placeholder = {
                Text("Search")
            },
            value = state.searchBarText,
            onValueChange = {
                onAction(CryptoListAction.OnSearchTextChanged(it))
            },
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Gray
            )
        )

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(CryptoListAction.OnRefresh) }
        ) {
            LazyColumn {
                items(state.cryptosFiltered){ crypto ->
                    CryptoListItem(
                        modifier = Modifier.clickable(
                            onClick = {onAction(CryptoListAction.OnCryptoClicked(crypto.id))}
                        ),
                        crypto = crypto
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    CryptoBuddyTheme {
        CryptoListScreen(
            state = CryptoListState(
                cryptosFiltered = listOf(
                    getDummyCrypto(),
                    getDummyCrypto(),
                    getDummyCrypto(),
                    getDummyCrypto(),
                    getDummyCrypto()
                )
            ),
            onAction = {}
        )
    }
}
