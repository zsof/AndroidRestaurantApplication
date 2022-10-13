package hu.zsof.restaurantapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.LoginFragmentBinding
import hu.zsof.restaurantapp.util.extensions.safeNavigate

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
        val email = binding.emailEditText.toString()
        val password = binding.passwordEditText.toString()
        binding.logInBtn.setOnClickListener {
           /* lifecycleScope.launch {
                val response = viewModel.login(LoginDataRequest(email, password))
                if (response.success) {*/
                    safeNavigate(LoginFragmentDirections.actionLoginFrToListFr())
              /*  } else {
                    showToast(response.errorMessage, Toast.LENGTH_LONG)
                }
            }*/
        }
    }
}
