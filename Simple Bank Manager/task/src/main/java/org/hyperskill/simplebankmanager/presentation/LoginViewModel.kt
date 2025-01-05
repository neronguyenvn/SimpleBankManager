package org.hyperskill.simplebankmanager.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hyperskill.simplebankmanager.domain.User

class LoginViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val username = savedStateHandle
        .get<String>("username")
        .let { if (it.isNullOrEmpty()) "Lara" else it }

    private val password = savedStateHandle
        .get<String>("password")
        .let { if (it.isNullOrEmpty()) "1234" else it }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun login(user: User) {
        viewModelScope.launch {
            if (user.username != username || user.password != password) {
                _uiEvent.send(UiEvent.ShowLoginResult(false))
                return@launch
            }
            _uiEvent.send(UiEvent.ShowLoginResult(true))
        }
    }

    sealed interface UiEvent {
        data class ShowLoginResult(val isSuccessful: Boolean) : UiEvent
    }
}
