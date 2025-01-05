package org.hyperskill.simplebankmanager.presentation.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentViewBalanceBinding

class ViewBalanceFragment : Fragment() {

    private val binding by lazy { FragmentViewBalanceBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<BalanceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupBalance()
    }

    private fun setupBalance() {
        binding.viewBalanceAmountTextView.text = getString(
            R.string.balance_amount_f, viewModel.balance
        )
    }
}
