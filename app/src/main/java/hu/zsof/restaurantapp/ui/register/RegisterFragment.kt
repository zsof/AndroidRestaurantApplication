package hu.zsof.restaurantapp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.RegisterFragmentBinding
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.util.extensions.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: RegisterFragmentBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegister()
        setupBindings()
    }

    private fun setupBindings() {
        binding.apply {
            loginText.setOnClickListener {
                safeNavigate(RegisterFragmentDirections.actionLoginFrToRegisterFr())
            }
        }
    }

    // todo jelszó mégegyszer - ellenőrini h ugyanaz-e -> backend
    private fun setupRegister() {
        binding.registerBtn.setOnClickListener {
            if (validateRegister()) {
                lifecycleScope.launch {
                    val email = binding.emailEditText.text.toString()
                    val password = binding.passwordEditText.text.toString()
                    val name = binding.userNameEditText.text.toString()
                    val nickname = binding.userNickNameEditText.text.toString()
                    val response =
                        viewModel.register(LoginDataRequest(email, password, name, nickname))
                    if (response.isSuccess) {
                        showToast(response.success, Toast.LENGTH_LONG)
                        safeNavigate(RegisterFragmentDirections.actionRegisterFrToListFr())
                    } else showToast(response.error, Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun validateRegister(): Boolean {
        binding.apply {
            return emailEditText.validateNonEmptyField() &&
                emailEditText.isEmailValid() &&
                passwordEditText.validateNonEmptyField() &&
                passwordEditText.isPasswordValid() &&
                userNameEditText.validateNonEmptyField()
        }
    }
}
