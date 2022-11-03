package hu.zsof.restaurantapp.ui.settings

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.SettingsFragmentBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding

    val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        //setupBindings()
    }

    private fun subscribeToObservers() {
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            binding.apply {
                settingsNameText.text = user.name ?: "Nincs megadva"
                settingsEmailText.text = user.email
                settingsNickNameText.text = user.nickName ?: "Nincs megadva"
            }
        }
    }

   /* private fun setupBindings() {
        binding.apply {
            settingsNameText.setOnClickListener {
                showNameChangeDialog(
                    title = "Név",
                    inputTextHint = "Név",
                    inputTextType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                )
            }
        }
    }

    private fun showNameChangeDialog(
        title: String?,
        onNegativeButton: () -> Unit = this::onDestroy,
        inputTextHint: String?,
        inputTextType: Int
    ) {
        val inputText = EditText(context)
        inputText.hint = inputTextHint
        inputText.inputType = inputTextType

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(getString(R.string.save_btn)) { dialog, _ ->
                dialog.cancel()
                if (!inputText.text.isNullOrEmpty()) {
                    binding.settingsNameText.text = inputText.text.toString()
                }
            }
            .setNegativeButton(getString(R.string.cancel_btn)) { _, _ -> onNegativeButton() }
            .setView(inputText)
            .create()
        alertDialog.show()
    }*/
}
