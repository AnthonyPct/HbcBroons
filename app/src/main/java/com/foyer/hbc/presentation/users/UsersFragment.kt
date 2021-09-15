package com.foyer.hbc.presentation.users

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseFragment
import com.foyer.hbc.common.containsResult
import com.foyer.hbc.common.getBackStackEntry
import com.foyer.hbc.common.getNavigationDialogResult
import com.foyer.hbc.common.removeResult
import com.foyer.hbc.databinding.FragmentUsersBinding
import com.foyer.hbc.domain.data.Result
import com.foyer.hbc.domain.data.users.PaymentState
import com.foyer.hbc.domain.data.users.UsersState
import com.foyer.hbc.domain.model.PaymentType
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.presentation.PasswordDialogFragment
import com.foyer.hbc.presentation.common.GridSpacingItemDecoration
import com.foyer.hbc.presentation.home.HomeViewModel
import com.foyer.hbc.presentation.users.payment.PaymentAdapter
import com.foyer.hbc.presentation.users.select.ISelectUserContract
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment :
    BaseFragment<FragmentUsersBinding>(),
    ISelectUserContract.ViewEvent,
    IUsersContract.ViewNavigation,
    IUsersContract.ViewCapabilities,
    CompoundButton.OnCheckedChangeListener,
    TextWatcher {

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val usersViewModel: UsersViewModel by viewModel()
    private val mainViewModel: HomeViewModel by navGraphViewModels(R.id.nav_graph)

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var userAdapter: UserAdapter
    private lateinit var paymentAdater: PaymentAdapter
    private var isPaymentBeingAdding: Boolean = false

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        private const val COLUMN_NUMBER = 5
        private const val USER_SPACING = 40
    }

    override fun getViewBinding(): FragmentUsersBinding {
        return FragmentUsersBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun initUI() {
        initUsersRecyclerView()
        initUserPaymentRecyclerView()
        binding?.userCreditView?.userCreditButton?.setOnClickListener {
            binding?.userCreditView?.userCreditAmount?.clearFocus()
            showPasswordDialog()
        }
        binding?.userCreditView?.userCreditClose?.setOnClickListener { closeCreditView() }
        binding?.userCreditView?.userCreditCardCheckbox?.setOnCheckedChangeListener(this)
        binding?.userCreditView?.userCreditCashCheckbox?.setOnCheckedChangeListener(this)
        binding?.userCreditView?.userCreditAmount?.addTextChangedListener(this)
        binding?.headerAddButton?.setOnClickListener { redirectToAddUSerPopup() }
    }

    override fun initData() {
        usersViewModel.getUsers()
    }

    override fun collectData() {
        lifecycleScope.launch {
            usersViewModel.users.collect {
                when (it) {
                    UsersState.Loading -> showLoader()
                    UsersState.Error -> {
                        hideLoader()
                        binding?.usersError?.visibility = View.VISIBLE
                    }
                    is UsersState.Success -> {
                        if (isPaymentBeingAdding.not()) hideLoader()
                        userAdapter.setData(it.users)
                    }
                }
            }
        }

        lifecycleScope.launch {
            usersViewModel.paymentState.collect { state ->
                when (state) {
                    is PaymentState.HasPayments -> {
                        binding?.userCreditView?.userPayments?.visibility = View.VISIBLE
                        binding?.userCreditView?.userNoCredit?.visibility = View.GONE
                        paymentAdater.setData(state.payments)
                    }
                    is PaymentState.Success -> {
                        hideLoader()
                        showSuccessCreditPopup()
                    }
                    PaymentState.Loading -> showLoader()
                    PaymentState.Error -> {
                        hideLoader()
                        showFailedPasswordPopup()
                    }
                    PaymentState.NoPaymentAvailable -> {
                        hideLoader()
                        binding?.userCreditView?.userPayments?.visibility = View.INVISIBLE
                        binding?.userCreditView?.userNoCredit?.visibility = View.VISIBLE
                    }
                    PaymentState.InitialState -> Unit // Do nothing
                }
            }
        }
    }

    override fun observeOnActivityResult() {
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = getBackStackEntry()

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && containsResult(PasswordDialogFragment.PASSWORD_KEY_RESULT)
            ) {
                when (getNavigationDialogResult<String>(PasswordDialogFragment.PASSWORD_KEY_RESULT)) {
                    Result.OK.key -> addPayment()
                    Result.KO.key -> showFailedPasswordPopup()
                }
                removeResult<String>(PasswordDialogFragment.PASSWORD_KEY_RESULT)
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
    // CONTRACT IMPLEMENTATION - ITEM EVENT
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickUser(userEntity: UserEntity) {
        mainViewModel.userSelected = userEntity
        userAdapter.updateSelectedUser(userEntity)
        initCreditView(userEntity)
        openCreditView(userEntity)
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - VIEW CAPABILITIES
    ///////////////////////////////////////////////////////////////////////////

    override fun initCreditView(user: UserEntity) {
        binding?.userCreditView?.run {
            userName.text = user.nom
            userTeam.text = user.equipe
            userBalance.text = user.balance.toString()
            userConsumption.text = user.consumptionsPayed.toString()
        }
        usersViewModel.getPayments(user)
    }

    override fun openCreditView(user: UserEntity) {
        if (!usersViewModel.isCreditPopupOpen) {
            binding?.root?.transitionToEnd()
            usersViewModel.isCreditPopupOpen = true
        }
    }

    override fun closeCreditView() {
        binding?.root?.transitionToStart()
        usersViewModel.isCreditPopupOpen = false
        userAdapter.unSelectUser()
    }

    override fun showPasswordDialog() {
        val action = UsersFragmentDirections.actionUsersFragmentToPasswordDialogFragment()
        findNavController().navigate(action)
    }

    override fun showSuccessCreditPopup() {
        closeCreditView()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.password_dialog_Success)
            .create()
            .show()
    }

    override fun showFailedPasswordPopup() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.password_dialog_error)
            .create()
            .show()
    }

    override fun showGeneralErrorPopup() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.general_error)
            .create()
            .show()
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - VIEW NAVIGATION
    ///////////////////////////////////////////////////////////////////////////

    override fun redirectToAddUSerPopup() {
        val action = UsersFragmentDirections.actionUsersFragmentToAddUserFragment()
        findNavController().navigate(action)
    }

    ///////////////////////////////////////////////////////////////////////////
    // OnCheckedChangeListener / TextWatcher
    ///////////////////////////////////////////////////////////////////////////

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            when (p0) {
                binding?.userCreditView?.userCreditCardCheckbox -> {
                    binding?.userCreditView?.userCreditCashCheckbox?.isChecked = false
                    usersViewModel.paymentMode = PaymentType.CB
                }
                binding?.userCreditView?.userCreditCashCheckbox -> {
                    binding?.userCreditView?.userCreditCardCheckbox?.isChecked = false
                    usersViewModel.paymentMode = PaymentType.CASH
                }
            }
        }
        updateCreditButton()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // Do nothing
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        usersViewModel.creditAmount = p0?.toString()?.toDoubleOrNull()
        updateCreditButton()
    }

    override fun afterTextChanged(p0: Editable?) {
        // Do nothing
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun initUsersRecyclerView() {
        userAdapter = UserAdapter(this)
        binding?.usersRecyclerview?.apply {
            addItemDecoration(GridSpacingItemDecoration(COLUMN_NUMBER, USER_SPACING, true))
            layoutManager = GridLayoutManager(
                requireContext(),
                COLUMN_NUMBER
            )
            adapter = userAdapter
        }
    }

    private fun initUserPaymentRecyclerView() {
        paymentAdater = PaymentAdapter()
        binding?.userCreditView?.userPayments?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paymentAdater
        }
    }

    private fun updateCreditButton() {
        binding?.userCreditView?.userCreditButton?.isEnabled =
            (usersViewModel.creditAmount != null &&
                    (binding?.userCreditView?.userCreditCashCheckbox?.isChecked == true ||
                            binding?.userCreditView?.userCreditCardCheckbox?.isChecked == true))
    }

    private fun addPayment() {
        isPaymentBeingAdding = true
        mainViewModel.userSelected?.let {
            usersViewModel.addUserPayment(
                usersViewModel.creditAmount!!,
                it,
                usersViewModel.paymentMode
            )
        }
    }
}