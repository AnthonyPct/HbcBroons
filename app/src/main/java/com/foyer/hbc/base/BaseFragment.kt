package com.foyer.hbc.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.foyer.hbc.presentation.home.HomeActivity

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    var binding: B? = null

    ///////////////////////////////////////////////////////////////////////////
    // ABSTRACT
    ///////////////////////////////////////////////////////////////////////////

    abstract fun getViewBinding(): B

    abstract fun initUI()
    abstract fun initData()
    abstract fun collectData()
    open fun observeOnActivityResult() {}

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentLifecycle", "onCreate ${this.javaClass}")
        initUI()
        initData()
        collectData()
        observeOnActivityResult()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("FragmentLifecycle", "onDestroy ${this.javaClass}")
        binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? HomeActivity)?.run {
            updateNavigationBar(this@BaseFragment)
        }
        Log.d("FragmentLifecycle", "onResume ${this.javaClass}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("FragmentLifecycle", "onPause ${this.javaClass}")
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    protected fun showLoader() {
        (activity as? HomeActivity)?.run {
            showLoader(true)
        }
    }

    protected fun hideLoader() {
        (activity as? HomeActivity)?.run {
            showLoader(false)
        }
    }
}
