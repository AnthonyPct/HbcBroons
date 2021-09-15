package com.foyer.hbc.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<B : ViewBinding> : DialogFragment() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    var binding: B? = null

    ///////////////////////////////////////////////////////////////////////////
    // ABSTRACT
    ///////////////////////////////////////////////////////////////////////////

    abstract fun getViewBinding(): B

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = getViewBinding()
        return AlertDialog.Builder(requireContext())
            .setView(binding?.root)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun dismiss() {
        super.dismiss()
        binding = null
    }
}