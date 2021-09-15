package com.foyer.hbc.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.foyer.hbc.NavGraphDirections
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseActivity
import com.foyer.hbc.databinding.ActivityHomeBinding

class HomeActivity :
    BaseActivity<ActivityHomeBinding>(),
    NavigationBarListener {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var navController: NavController

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.leftNavigationBar.bind(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun showLoader(toShow: Boolean) {
        if (toShow) {
            binding.progressLayout.visibility = View.VISIBLE
        } else {
            binding.progressLayout.visibility = View.GONE
        }
    }

    fun updateNavigationBar(fragment: Fragment) {
        binding.leftNavigationBar.updateButton(fragment)
    }

    private fun navigate(action: NavDirections) {
        navController.navigate(action)
    }

    ///////////////////////////////////////////////////////////////////////////
    // NAVIGATION LISTENER IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickHome() {
        navigate(NavGraphDirections.actionGlobalDashboardFragment())
    }

    override fun onClickStat() {
        navigate(NavGraphDirections.actionGlobalStatisticFragment())
    }

    override fun onClickUsers() {
        navigate(NavGraphDirections.actionGlobalUsersFragment())
    }

    override fun onClickResult() {
        navigate(NavGraphDirections.actionGlobalResumeFragment())
    }

    override fun onClickMatch() {
        navigate(NavGraphDirections.actionGlobalMatchsFagment())
    }
}
