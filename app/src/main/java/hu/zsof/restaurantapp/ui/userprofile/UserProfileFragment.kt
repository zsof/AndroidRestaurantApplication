package hu.zsof.restaurantapp.ui.userprofile

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.UserProfileFragmentBinding
import hu.zsof.restaurantapp.network.request.UserUpdateProfileRequest
import hu.zsof.restaurantapp.util.extensions.isEmailValid

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private lateinit var binding: UserProfileFragmentBinding

    val viewModel: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBindings()
    }

    private fun subscribeToObservers() {
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            binding.apply {
                userProfileNameText.text = user.name
                userProfileEmailText.text = user.email
                userProfileNickNameText.text = user.nickName
            }
        }
    }

    private fun setupBindings() {
        binding.apply {
            userProfileNickNameText.setOnClickListener {
                showDataChangeDialog(
                    title = "NickName",
                    changedTextView = userProfileNickNameText,
                    inputTextHint = "Type your nickname",
                    inputTextType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                )
            }
            userProfileNameText.setOnClickListener {
                showDataChangeDialog(
                    title = "Name",
                    changedTextView = userProfileNameText,
                    inputTextHint = "Type your name",
                    inputTextType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                )
            }
            userProfileEmailText.setOnClickListener {
                showDataChangeDialog(
                    title = "Email address",
                    changedTextView = userProfileEmailText,
                    inputTextHint = "Type your email address",
                    inputTextType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                )
            }
            // todo password - jelenlegi megadása, új jelszó, új jelszó még egyszer
        }
    }

    // todo visszaigazoló email/email megerősítése...
    private fun validateEmail(editText: EditText): Boolean {
        return editText.isEmailValid()
    }

    private fun showDataChangeDialog(
        title: String?,
        changedTextView: TextView,
        onNegativeButton: () -> Unit = this::onDestroy,
        inputTextHint: String?,
        inputTextType: Int
    ) {
        val inputText = EditText(requireContext())
        inputText.hint = inputTextHint
        inputText.inputType = inputTextType

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(getString(R.string.save_btn)) { dialog, _ ->
                dialog.cancel()
                if (inputText.text.isNotEmpty()) {
                    if (changedTextView == binding.userProfileEmailText) {
                        if (validateEmail(inputText)) {
                            changedTextView.text = inputText.text.toString()
                        } else changedTextView.text = binding.userProfileEmailText.text
                    } else {
                        changedTextView.text = inputText.text.toString()
                    }
                    viewModel.updateUserProfile(
                        UserUpdateProfileRequest(
                            name = binding.userProfileNameText.text.toString(),
                            nickName = binding.userProfileNickNameText.text.toString(),
                            email = binding.userProfileEmailText.text.toString()
                        )
                    )
                }
            }
            .setNegativeButton(getString(R.string.cancel_btn)) { _, _ -> onNegativeButton() }
            .setView(inputText)
            .create()
        alertDialog.show()
    }

    // todo toggle-hez külön mentés gomb
    // todo bekötni a toggle-ket -> backend is
    // todo image
    // todo dark theme
}
