package com.cequea.cryptobuddy.ui.screens.cryptoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cequea.cryptobuddy.domain.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val repository: CryptoRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            getCryptos()
            observeCryptos()
        }
    }

    private var hasLoadedInitialData = false

    private val _onNavigateToDetail = Channel<Int>()
    val onNavigateToDetail = _onNavigateToDetail.receiveAsFlow()

    private val _cryptoState = MutableStateFlow(CryptoListState())
    val cryptoState: StateFlow<CryptoListState> = _cryptoState.asStateFlow()

    private val _state = MutableStateFlow(CryptoListState())
    val state = _state.asStateFlow()

    fun onAction(action: CryptoListAction) {
        when (action) {
            is CryptoListAction.OnCryptoClicked -> {
                _onNavigateToDetail.trySend(action.cryptoId)
            }

            is CryptoListAction.OnSearchTextChanged -> {
                _state.update { it.copy(searchBarText = action.text) }
                filterCryptos()
            }

            CryptoListAction.OnRefresh -> {
                viewModelScope.launch {
                    _state.update { it.copy(isRefreshing = true) }
                    getCryptos()
                    _state.update { it.copy(isRefreshing = false) }
                }
            }
        }
    }

    private suspend fun observeCryptos() {
        repository
            .getCryptoFlow()
            .onStart {
                _state.update { it.copy(isLoading = true) }
            }
            .flowOn(Dispatchers.IO)
            .catch { e ->
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
            }
            .collect { cryptos ->
                _state.update {
                    it.copy(
                        cryptos = cryptos,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
                filterCryptos()
            }
    }


    private suspend fun getCryptos() {
        val cryptos = repository.getAllCryptos()
//        _state.update {
//            it.copy(
//                cryptos = cryptos,
//                cryptosFiltered = cryptos,
//                isLoading = false
//            )
//        }
        hasLoadedInitialData = true
    }

    private fun filterCryptos() {
        viewModelScope.launch {
            val filter = _state.value.searchBarText

            val filteredCryptos = if (filter.isBlank()) {
                _state.value.cryptos
            } else {
                _state.value.cryptos.filter {
                    it.name.contains(filter, ignoreCase = true) ||
                            it.symbol.contains(filter, ignoreCase = true)
                }
            }
            _state.update { it.copy(cryptosFiltered = filteredCryptos) }
        }
    }
}
