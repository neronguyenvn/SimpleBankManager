package org.hyperskill.simplebankmanager.domain

data class TransferFundsTransaction(
    val accountNumber: String,
    val amount: Double?,
)
