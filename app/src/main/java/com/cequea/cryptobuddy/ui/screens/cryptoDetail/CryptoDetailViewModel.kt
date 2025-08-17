package com.cequea.cryptobuddy.ui.screens.cryptoDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cequea.cryptobuddy.domain.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoDetailViewModel(
    private val cryptoRepository: CryptoRepository
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CryptoDetailState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CryptoDetailState()
        )

    fun onAction(action: CryptoDetailAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    fun getCryptoById(cryptoId: Int){
        viewModelScope.launch {
            val crypto = cryptoRepository.getCryptoById(cryptoId)
            _state.update { it.copy(crypto = crypto) }
        }
    }

}
