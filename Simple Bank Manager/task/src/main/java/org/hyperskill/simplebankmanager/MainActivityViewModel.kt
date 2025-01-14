package org.hyperskill.simplebankmanager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.hyperskill.simplebankmanager.domain.TransferFundsTransaction
import org.hyperskill.simplebankmanager.domain.User
import org.hyperskill.simplebankmanager.util.asResult

class MainActivityViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val username = savedStateHandle
        .get<String>("username")
        .let { if (it.isNullOrEmpty()) DEFAULT_USERNAME else it }

    private val password = savedStateHandle
        .get<String>("password")
        .let { if (it.isNullOrEmpty()) DEFAULT_PASSWORD else it }

    private val exchangeMap = savedStateHandle
        .get<Map<String, Map<String, Double>>>("exchangeMap")
        ?: defaultMap

    private val balance: Double = savedStateHandle.get<Double>("balance") ?: DEFAULT_BALANCE

    var currentUser: User? = null
        private set

    fun login(
        username: String,
        password: String,
    ): Result<Unit> {
        if (username != this@MainActivityViewModel.username ||
            password != this@MainActivityViewModel.password
        ) {
            return false.asResult()
        }
        currentUser = User(
            username = username,
            password = password,
            balance = balance
        )
        return true.asResult()
    }

    fun transferFunds(transaction: TransferFundsTransaction) {
        currentUser = currentUser!!.copy(
            balance = currentUser!!.balance - transaction.amount!!
        )
    }

    fun exchangeMoney(
        amount: Double,
        fromUnit: String,
        toUnit: String
    ): Double {
        val rate = exchangeMap[fromUnit]?.get(toUnit)
        requireNotNull(rate) { "Exchange rate not found for $fromUnit to $toUnit" }
        return amount * rate
    }

    companion object {
        private const val DEFAULT_USERNAME = "Lara"
        private const val DEFAULT_PASSWORD = "1234"
        private const val DEFAULT_BALANCE = 100.0

        private val defaultMap = mapOf(
            "EUR" to mapOf(
                "GBP" to 0.5,
                "USD" to 2.0
            ),
            "GBP" to mapOf(
                "EUR" to 2.0,
                "USD" to 4.0
            ),
            "USD" to mapOf(
                "EUR" to 0.5,
                "GBP" to 0.25
            )
        )
    }
}
