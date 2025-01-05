package org.hyperskill.simplebankmanager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.MainActivityViewModel
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentUserMenuBinding

class UserMenuFragment : Fragment() {

    private val binding by lazy { FragmentUserMenuBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupCurrentUserName()
        setupListeners()
    }

    private fun setupCurrentUserName() {
        binding.userMenuWelcomeTextView.text = getString(
            R.string.welcome_s, viewModel.currentUser!!.username
        )
    }

    private fun setupListeners() = with(binding) {
        userMenuViewBalanceButton.setOnClickListener {
            val destination = R.id.action_userMenuFragment_to_viewBalanceFragment
            findNavController().navigate(destination)
        }
        userMenuTransferFundsButton.setOnClickListener {
            val destination = R.id.action_userMenuFragment_to_transferFundsFragment
            findNavController().navigate(destination)
        }
    }
}
