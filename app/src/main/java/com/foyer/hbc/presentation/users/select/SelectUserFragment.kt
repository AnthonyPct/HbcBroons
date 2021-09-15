package com.foyer.hbc.presentation.users.select

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseDialogFragment
import com.foyer.hbc.common.setNavigationResult
import com.foyer.hbc.databinding.FragmentValidateOrderBinding
import com.foyer.hbc.domain.data.Result
import com.foyer.hbc.domain.data.dashboard.SelectUserState
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.presentation.home.HomeViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectUserFragment :
    BaseDialogFragment<FragmentValidateOrderBinding>(),
    ISelectUserContract.ViewEvent {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        const val SELECT_USER_KEY = "SELECT_USER_KEY"
    }

    override fun getViewBinding(): FragmentValidateOrderBinding {
        return FragmentValidateOrderBinding.inflate(LayoutInflater.from(context))
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val selectUserViewModel: SelectUserViewModel by viewModel()
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var selectUserAdapter: SelectUserAdapter

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        initRecyclerView()
        lifecycleScope.launchWhenCreated {
            selectUserViewModel.selectUserState.collect {
                when (it) {
                    is SelectUserState.HasUsers -> {
                        showProgressBar(false)
                        selectUserAdapter.setData(it.users)
                    }
                    is SelectUserState.Error -> {
                        showProgressBar(false)
                        binding?.validateOrderError?.visibility = View.VISIBLE
                    }
                    is SelectUserState.Loading -> showProgressBar(true)
                }
            }
        }

        selectUserViewModel.getUsers()
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun initRecyclerView() {
        selectUserAdapter = SelectUserAdapter(this)
        binding?.validateOrderUsersRecyclerview?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = selectUserAdapter
        }
    }

    private fun showProgressBar(shouldShow: Boolean) {
        binding?.valdiateOrderUsersProgressBar?.visibility =
            if (shouldShow) View.VISIBLE else View.GONE
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - ViewEvent
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickUser(userEntity: UserEntity) {
        homeViewModel.userSelected = userEntity
        setNavigationResult(
            result = Result.OK.key,
            key = SELECT_USER_KEY
        )
    }
}
