package hu.zsof.restaurantapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.LoginFragmentBinding
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.util.Constants.Prefs.USER_DATA
import hu.zsof.restaurantapp.util.extensions.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBindings()
        setupLogin()
    }

    private fun setupBindings() {
        binding.apply {
            registerText.setOnClickListener {
                safeNavigate(LoginFragmentDirections.actionLoginFrToRegisterFr())
            }
        }
    }

    private fun setupLogin() {
        binding.logInBtn.setOnClickListener {
            if (this.checkForInternet(requireContext())) {
                lifecycleScope.launch {
                    if (validateLogin()) {
                        val email = binding.emailEditText.text.toString()
                        val password = binding.passwordEditText.text.toString()
                        val response =
                            viewModel.login(LoginDataRequest(email = email, password = password))
                        if (response.isSuccess) {
                            showToast(response.successMessage, Toast.LENGTH_LONG)
                            safeNavigate(LoginFragmentDirections.actionLoginFrToListFr())
                        } else {
                            showToast(response.error, Toast.LENGTH_LONG)
                        }

                        val userJson = Gson().toJson(response.user)
                        viewModel.setAppPreference(USER_DATA, userJson)
                    }
                }
            } else showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun validateLogin(): Boolean {
        binding.apply {
            return emailEditText.validateNonEmptyField() && emailEditText.isEmailValid() && passwordEditText.validateNonEmptyField()
        }
    }
}
