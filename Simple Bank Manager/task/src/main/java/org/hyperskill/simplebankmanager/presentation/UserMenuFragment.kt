package org.hyperskill.simplebankmanager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentUserMenuBinding

class UserMenuFragment : Fragment() {

    private val binding by lazy { FragmentUserMenuBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getCurrentUser()?.let {
            binding.userMenuWelcomeTextView.text = getString(
                R.string.welcome_s, it.username
            )
        }
    }
}
