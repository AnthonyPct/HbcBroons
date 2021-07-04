package com.example.hbc.presentation.home

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.hbc.R
import com.example.hbc.base.BaseActivity
import com.example.hbc.databinding.ActivityHomeBinding

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
    // NAVIGATION LISTENER IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickHome() {
        navController.navigate(R.id.action_statisticFragment_to_dashboardFragment)
    }

    override fun onClickStat() {
        navController.navigate(R.id.action_dashboardFragment_to_statisticFragment)
    }

    override fun onClickUsers() {
        TODO("Not yet implemented")
    }

    override fun onClickResult() {
        TODO("Not yet implemented")
    }

    override fun onClickMatch() {
        TODO("Not yet implemented")
    }
}
