package com.foyer.hbc.presentation.users.add

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.CompoundButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseDialogFragment
import com.foyer.hbc.databinding.FragmentAddUserBinding
import com.foyer.hbc.domain.data.users.AddUserState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUserFragment :
    BaseDialogFragment<FragmentAddUserBinding>(),
    AddUserContract.ViewCapabilities,
    CompoundButton.OnCheckedChangeListener,
    TextWatcher {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getViewBinding(): FragmentAddUserBinding {
        return FragmentAddUserBinding.inflate(LayoutInflater.from(context))
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val viewModel: AddUserViewModel by viewModel()

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        binding?.userAddOtherBox?.setOnCheckedChangeListener(this)
        binding?.userAddSgBox?.setOnCheckedChangeListener(this)
        binding?.userAddSfBox?.setOnCheckedChangeListener(this)
        binding?.userPseudo?.addTextChangedListener(this)
        binding?.userAddValidate?.setOnClickListener { addUser() }

        lifecycleScope.launch {
            viewModel.addUserState.collect {
                when (it) {
                    AddUserState.Success -> {
                        findNavController().popBackStack()
                    }
                    AddUserState.Error -> {
                        showFailedPopup()
                    }
                    AddUserState.InitialState -> {
                        // do nothing
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - VIEW CAPABILITIES
    ///////////////////////////////////////////////////////////////////////////

    override fun showFailedPopup() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.user_add_title)
            setMessage(R.string.user_add_error)
            setPositiveButton(
                R.string.common_validate
            ) { _, _ ->
                findNavController().popBackStack()
            }
        }
            .create()
            .show()
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - WIDGET
    ///////////////////////////////////////////////////////////////////////////

    override fun onCheckedChanged(checkbox: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            when (checkbox) {
                binding?.userAddOtherBox -> {
                    binding?.userAddSfBox?.isChecked = false
                    binding?.userAddSgBox?.isChecked = false
                }
                binding?.userAddSfBox -> {
                    binding?.userAddOtherBox?.isChecked = false
                    binding?.userAddSgBox?.isChecked = false
                }
                binding?.userAddSgBox -> {
                    binding?.userAddOtherBox?.isChecked = false
                    binding?.userAddSfBox?.isChecked = false
                }
            }
        }
        updateValidateButton()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // Do nothing
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        updateValidateButton()
    }

    override fun afterTextChanged(p0: Editable?) {
        // Do nothing
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun addUser() {
        binding?.userPseudo?.text?.toString()?.let { viewModel.addUser(it, getTeam()) }
    }

    private fun updateValidateButton() {
        binding?.userAddValidate?.isEnabled =
            oneCheckboxChecked() && binding?.userPseudo?.text?.isNotEmpty() == true
    }

    private fun oneCheckboxChecked(): Boolean =
        binding?.userAddOtherBox?.isChecked == true ||
                binding?.userAddSfBox?.isChecked == true ||
                binding?.userAddSgBox?.isChecked == true

    private fun getTeam(): String {
        return when {
            binding?.userAddOtherBox?.isChecked == true -> "Autre"
            binding?.userAddSfBox?.isChecked == true -> "SF"
            binding?.userAddSgBox?.isChecked == true -> "SG"
            else -> ""
        }
    }
}