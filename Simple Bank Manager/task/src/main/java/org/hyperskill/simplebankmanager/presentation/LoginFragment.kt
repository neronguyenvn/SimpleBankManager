package org.hyperskill.simplebankmanager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.simplebankmanager.MainActivityViewModel
import org.hyperskill.simplebankmanager.MainActivityViewModel.UiEvent
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentLoginBinding
import org.hyperskill.simplebankmanager.util.launchOnLifecycleState

class LoginFragment : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginButton.setOnClickListener {
            viewModel.login(
                username = binding.loginUsername.text.toString(),
                password = binding.loginPassword.text.toString()
            )
        }

        viewLifecycleOwner.launchOnLifecycleState {
            collectAndHandleUiEvents()
        }
    }

    private fun CoroutineScope.collectAndHandleUiEvents() {
        viewModel.uiEvent.onEach { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowLoginResult -> handleLoginResult(uiEvent.isSuccessful)
                // Handle other UI events if necessary
            }
        }.launchIn(this)
    }

    private fun handleLoginResult(isSuccessful: Boolean) {
        val message = if (isSuccessful) "logged in" else "invalid credentials"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        if (isSuccessful) {
            findNavController().navigate(R.id.action_loginFragment_to_userMenuFragment)
        }
    }
}
