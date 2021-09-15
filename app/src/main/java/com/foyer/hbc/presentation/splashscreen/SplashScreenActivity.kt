package com.foyer.hbc.presentation.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.foyer.hbc.BuildConfig
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseActivity
import com.foyer.hbc.databinding.ActivitySplashscreenBinding
import com.foyer.hbc.presentation.home.HomeActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : BaseActivity<ActivitySplashscreenBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val viewModel: SplashScreenViewModel by viewModel()

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getViewBinding(): ActivitySplashscreenBinding {
        return ActivitySplashscreenBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.version.text = BuildConfig.VERSION_NAME
        lifecycleScope.launch {
            viewModel.data.collect {
                when (it) {
                    SplashScreenState.Loading -> binding.progress.visibility = View.VISIBLE
                    SplashScreenState.Success -> {
                        startActivity(
                            Intent(this@SplashScreenActivity, HomeActivity::class.java)
                        )
                        finish()
                    }
                    is SplashScreenState.Error -> {
                        binding.progress.visibility = View.GONE
                        binding.errorMessage.apply {
                            visibility = View.VISIBLE
                            text = if (it.isInternetAvailable) {
                                getString(R.string.splashscreen_error_message)
                            } else {
                                getString(R.string.splashscreen_error_internet_message)
                            }
                        }
                    }
                }
            }
        }
        viewModel.fetchAndSaveData()
    }
}
