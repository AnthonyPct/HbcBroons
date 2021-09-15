package com.foyer.hbc.presentation

import `in`.aabhasjindal.otptextview.OTPListener
import android.view.LayoutInflater
import com.foyer.hbc.base.BaseDialogFragment
import com.foyer.hbc.common.setNavigationResult
import com.foyer.hbc.databinding.FragmentPasswordDialogBinding
import com.foyer.hbc.domain.data.Result

class PasswordDialogFragment : BaseDialogFragment<FragmentPasswordDialogBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        private const val PASSWORD = "1313"
        const val PASSWORD_KEY_RESULT = "Password"
    }

    override fun getViewBinding(): FragmentPasswordDialogBinding {
        return FragmentPasswordDialogBinding.inflate(LayoutInflater.from(context))
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        binding?.passwordDialog?.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // Do nothing
            }

            override fun onOTPComplete(otp: String) {
                if (otp == PASSWORD) {
                    setNavigationResult(
                        result = Result.OK.key,
                        key = PASSWORD_KEY_RESULT
                    )
                } else {
                    setNavigationResult(
                        result = Result.KO.key,
                        key = PASSWORD_KEY_RESULT
                    )
                }
            }
        }
    }
}