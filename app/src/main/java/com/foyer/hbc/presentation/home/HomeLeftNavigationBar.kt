package com.foyer.hbc.presentation.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.foyer.hbc.databinding.NavigationViewBinding
import com.foyer.hbc.presentation.dashboard.DashboardFragment
import com.foyer.hbc.presentation.match.MatchsFagment
import com.foyer.hbc.presentation.resume.ResumeFragment
import com.foyer.hbc.presentation.stats.StatisticFragment
import com.foyer.hbc.presentation.users.UsersFragment

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

    fun updateButton(fragment: Fragment) {
        when (fragment) {
            is DashboardFragment -> selectButton(binding.homeDashboardButton)
            is StatisticFragment -> selectButton(binding.homeStatButton)
            is UsersFragment -> selectButton(binding.homeUserButton)
            is ResumeFragment -> selectButton(binding.homeResultsButton)
            is MatchsFagment -> selectButton(binding.homeMatchsButton)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun initListener() {
        binding.homeDashboardButton.setOnClickListener {
            listener?.onClickHome()
        }

        binding.homeStatButton.setOnClickListener {
            listener?.onClickStat()
        }

        binding.homeUserButton.setOnClickListener {
            listener?.onClickUsers()
        }

        binding.homeResultsButton.setOnClickListener {
            listener?.onClickResult()
        }

        binding.homeMatchsButton.setOnClickListener {
            listener?.onClickMatch()
        }
    }

    private fun selectButton(button: View) {
        listOf(
            binding.homeDashboardButton,
            binding.homeStatButton,
            binding.homeUserButton,
            binding.homeResultsButton,
            binding.homeMatchsButton
        ).forEach {
            it.isClickable = true
            it.isSelected = false
        }
        button.isSelected = true
        button.isClickable = false
    }
}
