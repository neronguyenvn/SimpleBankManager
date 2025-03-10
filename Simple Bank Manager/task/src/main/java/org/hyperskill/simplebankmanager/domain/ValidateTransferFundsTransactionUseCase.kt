package org.hyperskill.simplebankmanager.domain

import org.hyperskill.simplebankmanager.util.asResult

class ValidateTransferFundsTransactionUseCase {

    class InvalidAccountNumber : Exception()
    class InvalidAmount : Exception()
    class InvalidAccountNumberAndAmount : Exception()

    operator fun invoke(transaction: TransferFundsTransaction): Result<Unit> {
        return kotlin.runCatching {
            validateTransaction(transaction)
            true.asResult()
        }.getOrElse {
            Result.failure(it)
        }
    }

    private fun validateTransaction(transaction: TransferFundsTransaction) {
        if (transaction.accountNumber.isBlank()) {
            throw InvalidAccountNumber()
        }

        if (transaction.amount == null) {
            throw InvalidAmount()
        }

        if (validAccountNumberPrefixSet.none { transaction.accountNumber.startsWith(it) }) {
            throw InvalidAccountNumber()
        }

        if (transaction.accountNumber.count { it.isDigit() } != ACCOUNT_NUMBER_DIGIT_COUNT) {
            throw InvalidAccountNumber()
        }

        if (transaction.amount <= 0) {
            throw InvalidAmount()
        }
    }

    companion object {
        private val validAccountNumberPrefixSet = setOf("sa", "ca")
        private const val ACCOUNT_NUMBER_DIGIT_COUNT = 4
    }
}
