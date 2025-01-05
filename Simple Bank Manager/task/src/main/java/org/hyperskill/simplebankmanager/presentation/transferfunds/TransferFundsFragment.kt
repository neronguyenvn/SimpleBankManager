package org.hyperskill.simplebankmanager.presentation.transferfunds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.MainActivityViewModel
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentTransferFundsBinding
import org.hyperskill.simplebankmanager.domain.TransferFundsTransaction
import org.hyperskill.simplebankmanager.domain.ValidateTransferFundsTransactionUseCase
import org.hyperskill.simplebankmanager.domain.ValidateTransferFundsTransactionUseCase.InvalidAccountNumber
import org.hyperskill.simplebankmanager.domain.ValidateTransferFundsTransactionUseCase.InvalidAccountNumberAndAmount
import org.hyperskill.simplebankmanager.domain.ValidateTransferFundsTransactionUseCase.InvalidAmount

class TransferFundsFragment : Fragment() {

    private val binding by lazy { FragmentTransferFundsBinding.inflate(layoutInflater) }
    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        transferFundsButton.setOnClickListener {
            val accountNumber = transferFundsAccountEditText.text.toString()
            val amount = transferFundsAmountEditText.text.toString().toDoubleOrNull()
            val transaction = TransferFundsTransaction(
                accountNumber = accountNumber,
                amount = amount
            )
            val validateTransactionResult = ValidateTransferFundsTransactionUseCase().invoke(
                transaction = transaction
            )
            transferFundsAccountEditText.error = null
            transferFundsAmountEditText.error = null
            validateTransactionResult.onFailure { error ->
                when (error) {
                    is InvalidAccountNumberAndAmount -> {
                        transferFundsAccountEditText.error = "Invalid account number"
                        transferFundsAmountEditText.error = "Invalid amount"
                    }

                    is InvalidAccountNumber -> {
                        transferFundsAccountEditText.error = "Invalid account number"
                    }

                    is InvalidAmount -> {
                        transferFundsAmountEditText.error = "Invalid amount"
                    }
                }
                return@setOnClickListener
            }
            val currentBalance = mainActivityViewModel.currentUser!!.balance
            if (currentBalance < transaction.amount!!) {
                val message = "Not enough funds to transfer ${
                    getString(
                        R.string.transaction_amount_f,
                        transaction.amount
                    )
                }"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mainActivityViewModel.transferFunds(transaction)
            val message = "Transferred ${
                getString(
                    R.string.transaction_amount_f,
                    transaction.amount
                )
            } to account ${transaction.accountNumber}"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}
