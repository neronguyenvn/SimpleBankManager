package org.hyperskill.simplebankmanager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.simplebankmanager.MainActivityViewModel
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentPayBillsBinding
import org.hyperskill.simplebankmanager.util.showAlertDialog
import org.hyperskill.simplebankmanager.util.showShortToast

class PayBillsFragment : Fragment() {

    private val viewModel by activityViewModels<MainActivityViewModel>()
    private val binding by lazy {
        FragmentPayBillsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionListeners()
    }

    private fun setupActionListeners() = with(binding) {
        payBillsShowBillInfoButton.setOnClickListener {
            val billInfo = viewModel.getBillInfo(payBillsCodeInputEditText.text.toString())
            if (billInfo == null) {
                context?.showAlertDialog(
                    title = "Error",
                    message = "Wrong code",
                    positiveButtonText = "Ok",
                )
                return@setOnClickListener
            }

            val billInfoString = with(billInfo) {
                """
                Name: $first
                BillCode: $second
                Amount: ${getString(R.string.transaction_amount_f, third)}
            """.trimIndent()
            }

            context?.showAlertDialog(
                title = "Bill info",
                message = billInfoString,
                positiveButtonText = "Confirm",
                negativeButtonText = "Cancel"
            ) {
                viewModel.payBill(billInfo.second)
                    .onFailure {
                        context?.showAlertDialog(
                            title = "Error",
                            message = "${it.message}",
                            positiveButtonText = "Ok"
                        )

                    }
                    .onSuccess {
                        context?.showShortToast(it)
                    }
            }
        }
    }
}
