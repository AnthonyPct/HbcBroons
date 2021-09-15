package com.foyer.hbc.presentation.dashboard

import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseFragment
import com.foyer.hbc.common.containsResult
import com.foyer.hbc.common.getNavigationDialogResult
import com.foyer.hbc.common.removeResult
import com.foyer.hbc.common.withCurrency
import com.foyer.hbc.databinding.FragmentDashboardBinding
import com.foyer.hbc.domain.data.Result
import com.foyer.hbc.domain.data.dashboard.CarouselState
import com.foyer.hbc.domain.data.dashboard.HistoricState
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.presentation.common.SpacesItemDecoration
import com.foyer.hbc.presentation.dashboard.carousel.CarouselAdapter
import com.foyer.hbc.presentation.dashboard.historic.HistoricAdapter
import com.foyer.hbc.presentation.home.HomeViewModel
import com.foyer.hbc.presentation.users.select.SelectUserFragment.Companion.SELECT_USER_KEY
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(),
    IDashboardContract.ViewEvent.Historic,
    IDashboardContract.ViewEvent.Order,
    IDashboardContract.ViewCapabilities {

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val dashboardViewModel: DashboardViewModel by viewModel()
    private val mainViewModel: HomeViewModel by navGraphViewModels(R.id.nav_graph)

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var historicAdapter: HistoricAdapter

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getViewBinding(): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun initUI() {
        initCarouselRecyclerView()
        initHistoricRecyclerView()
        binding?.headerDate?.text = dashboardViewModel.getCurrentDate()
        binding?.dashboardBeerOrderComponent?.bind(this)
        binding?.dashboardSoftOrderComponent?.bind(this)
        binding?.dashboardEatOrderComponent?.bind(this)
        binding?.dashboardOrderButton?.setOnClickListener { onClickValidateOrder() }
    }

    override fun initData() {
        dashboardViewModel.getCarouselState()
        dashboardViewModel.getHistoricState()
    }

    override fun collectData() {
        lifecycleScope.launch {
            dashboardViewModel.carouselState.collect { state ->
                when (state) {
                    is CarouselState.Success -> {
                        carouselAdapter.setData(state.carouselCells)
                        hideLoader()
                    }
                    is CarouselState.Loading -> showLoader()
                    else -> {
                        hideLoader()
                        binding?.dashboardCarousel?.visibility = View.GONE
                    }
                }
            }
        }

        lifecycleScope.launch {
            dashboardViewModel.historicState.collect { state ->
                when (state) {
                    is HistoricState.Loading ->
                        binding?.dashboardHistoricLoader?.visibility = View.VISIBLE
                    is HistoricState.Success -> {
                        binding?.dashboardHistoricLoader?.visibility = View.GONE
                        historicAdapter.setItems(state.consumptions)
                    }
                    HistoricState.Error -> binding?.dashboardHistoricError?.visibility =
                        View.VISIBLE
                }
            }
        }
    }

    override fun observeOnActivityResult() {
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.dashboardFragment)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && containsResult(SELECT_USER_KEY)
            ) {
                val result = getNavigationDialogResult<String>(SELECT_USER_KEY)
                if (result == Result.OK.key) {
                    showConfirmationOrderPopup(mainViewModel.userSelected)
                }
                removeResult<String>(SELECT_USER_KEY)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun initCarouselRecyclerView() {
        carouselAdapter = CarouselAdapter()
        binding?.dashboardCarousel?.apply {
            addItemDecoration(
                SpacesItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.spacing_item_carousel)
                )
            )
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = carouselAdapter
        }
    }

    private fun initHistoricRecyclerView() {
        historicAdapter = HistoricAdapter(this)
        binding?.dashboardHistoric?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = historicAdapter
        }
    }

    private fun resetData() {
        dashboardViewModel.resetData()
        binding?.dashboardBeerOrderComponent?.reset()
        binding?.dashboardSoftOrderComponent?.reset()
        binding?.dashboardEatOrderComponent?.reset()
        updateOrderButton()
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - IDashboardContract.ViewEvent.Historic
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickConsumption(consumptionEntity: ConsumptionEntity) {
        // Nothing to do ?
    }

    override fun onClickFooter() {
        // Rediriger vers stats
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - IDashboardContract.ViewEvent.Order
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickAddBeer() {
        dashboardViewModel.addBeerToOrder()
        binding?.dashboardBeerOrderComponent?.updateAmount(dashboardViewModel.getTotalBeerPrice())
        updateOrderButton()
    }

    override fun onClickRemoveBeer() {
        dashboardViewModel.removeBeerToOrder()
        binding?.dashboardBeerOrderComponent?.updateAmount(dashboardViewModel.getTotalBeerPrice())
        updateOrderButton()
    }

    override fun onClickAddSoft() {
        dashboardViewModel.addSoftToOrder()
        binding?.dashboardSoftOrderComponent?.updateAmount(dashboardViewModel.getTotalSoftPrice())
        updateOrderButton()
    }

    override fun onClickRemoveSoft() {
        dashboardViewModel.removeSoftToOrder()
        binding?.dashboardSoftOrderComponent?.updateAmount(dashboardViewModel.getTotalSoftPrice())
        updateOrderButton()
    }

    override fun onClickAddEat() {
        dashboardViewModel.addEatToOrder()
        binding?.dashboardEatOrderComponent?.updateAmount(dashboardViewModel.getTotalEatPrice())
        updateOrderButton()
    }

    override fun onClickRemoveEat() {
        dashboardViewModel.removeEatToOrder()
        binding?.dashboardEatOrderComponent?.updateAmount(dashboardViewModel.getTotalEatPrice())
        updateOrderButton()
    }

    override fun onClickValidateOrder() {
        showSelectUserPopup()
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - ViewCapabilities
    ///////////////////////////////////////////////////////////////////////////

    override fun updateOrderButton() {
        dashboardViewModel.getTotalOrderPrice().takeIf { it > 0.0 }?.let {
            binding?.dashboardOrderButton?.apply {
                text = getString(
                    R.string.order_button_with_quantity,
                    dashboardViewModel.getTotalOrderPrice().toString().withCurrency()
                )
                isEnabled = true
            }
        } ?: let {
            binding?.dashboardOrderButton?.apply {
                text = getString(R.string.order_button)
                isEnabled = false
            }
        }
    }

    override fun showSelectUserPopup() {
        val action = DashboardFragmentDirections.actionDashboardFragmentToValidateOrderFragment()
        findNavController().navigate(action)
    }

    override fun showConfirmationOrderPopup(user: UserEntity?) {
        user?.let { userEntity ->
            AlertDialog.Builder(requireContext()).apply {
                setTitle(R.string.order_confirmation_popup_title)
                setMessage("${dashboardViewModel.getTotalOrderPrice()} € pour ${userEntity.nom}")
                setPositiveButton(R.string.common_validate) { _, _ ->
                    dashboardViewModel.addConsumption(userEntity, null)
                    resetData()
                }
                setNegativeButton(R.string.common_cancel) { _, _ ->
                    resetData()
                }
            }
                .create()
                .show()
        }
    }
}
