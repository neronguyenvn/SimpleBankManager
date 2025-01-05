package org.hyperskill.simplebankmanager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hyperskill.simplebankmanager.domain.TransferFundsTransaction
import org.hyperskill.simplebankmanager.domain.User

class MainActivityViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val username = savedStateHandle
        .get<String>("username")
        .let { if (it.isNullOrEmpty()) DEFAULT_USERNAME else it }

    private val password = savedStateHandle
        .get<String>("password")
        .let { if (it.isNullOrEmpty()) DEFAULT_PASSWORD else it }

    private val balance: Double = savedStateHandle.get<Double>("balance") ?: DEFAULT_BALANCE
    private val _uiEvent = Channel<UiEvent>()

    val uiEvent = _uiEvent.receiveAsFlow()
    var currentUser: User? = null
        private set

    fun login(
        username: String,
        password: String,
    ) {
        viewModelScope.launch {
            if (username != this@MainActivityViewModel.username ||
                password != this@MainActivityViewModel.password
            ) {
                _uiEvent.send(UiEvent.ShowLoginResult(false))
                return@launch
            }
            currentUser = User(
                username = username,
                password = password,
                balance = balance
            )
            _uiEvent.send(UiEvent.ShowLoginResult(true))
        }
    }

    fun transferFunds(transaction: TransferFundsTransaction) {
        currentUser = currentUser!!.copy(
            balance = currentUser!!.balance - transaction.amount!!
        )
    }

    sealed interface UiEvent {
        data class ShowLoginResult(val isSuccessful: Boolean) : UiEvent
    }

    companion object {
        private const val DEFAULT_USERNAME = "Lara"
        private const val DEFAULT_PASSWORD = "1234"
        private const val DEFAULT_BALANCE = 100.0
    }
}
