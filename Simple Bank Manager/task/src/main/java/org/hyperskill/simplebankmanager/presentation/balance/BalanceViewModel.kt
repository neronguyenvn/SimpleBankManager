package org.hyperskill.simplebankmanager.presentation.balance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class BalanceViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val balance: Double = savedStateHandle.get<Double>("balance") ?: DEFAULT_BALANCE

    companion object {
        private const val DEFAULT_BALANCE = 100.0
    }
}
