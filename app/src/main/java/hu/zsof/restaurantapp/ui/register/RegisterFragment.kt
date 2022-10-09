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
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.RegisterFragmentBinding
import hu.zsof.restaurantapp.network.model.LoginData
import hu.zsof.restaurantapp.util.extensions.safeNavigate
import hu.zsof.restaurantapp.util.extensions.showToast
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
    }

    private fun setupRegister() {
        val email = binding.emailEditText.toString()
        val password = binding.passwordEditText.toString()

        binding.logInBtn.setOnClickListener {
            lifecycleScope.launch {
                val response = viewModel.register(LoginData(email, password))
                if (response.success) {
                    safeNavigate(RegisterFragmentDirections.actionRegisterFrToListFr())
                } else showToast(response.errorMessage, Toast.LENGTH_LONG)
            }
        }
    }
}
