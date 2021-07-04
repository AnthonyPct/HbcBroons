package com.example.hbc.presentation.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.hbc.databinding.NavigationViewBinding

class HomeLeftNavigationBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var listener: NavigationBarListener? = null
    private var binding: NavigationViewBinding

    ///////////////////////////////////////////////////////////////////////////
    // FACTORY
    ///////////////////////////////////////////////////////////////////////////

    init {
        binding = NavigationViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        binding.homeDashboardButton.isSelected = true
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(listener: NavigationBarListener) {
        this.listener = listener
        initListener()
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun initListener() {
        binding.homeDashboardButton.setOnClickListener {
            binding.homeDashboardButton.isSelected = true
            binding.homeStatButton.isSelected = false
            listener?.onClickHome()
        }

        binding.homeStatButton.setOnClickListener {
            binding.homeStatButton.isSelected = true
            binding.homeDashboardButton.isSelected = false
            listener?.onClickStat()
        }
    }
}
