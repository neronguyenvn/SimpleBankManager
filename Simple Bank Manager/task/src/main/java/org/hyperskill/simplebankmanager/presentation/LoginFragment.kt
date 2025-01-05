package org.hyperskill.simplebankmanager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.simplebankmanager.databinding.FragmentLoginBinding
import org.hyperskill.simplebankmanager.domain.User
import org.hyperskill.simplebankmanager.presentation.LoginViewModel.UiEvent

class LoginFragment : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val user = User(
                username = binding.loginUsername.text.toString(),
                password = binding.loginPassword.text.toString()
            )
            viewModel.login(user)
        }

        viewLifecycleOwner.launchOnLifecycleState {
            collectAndHandleUiEvents()
        }
    }

    private fun CoroutineScope.collectAndHandleUiEvents() {
        viewModel.uiEvent.onEach { uiEvent ->
            if (uiEvent is UiEvent.ShowLoginResult) {
                val message =
                    if (uiEvent.isSuccessful) "logged in"
                    else "invalid credentials"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }.launchIn(this)
    }
}
