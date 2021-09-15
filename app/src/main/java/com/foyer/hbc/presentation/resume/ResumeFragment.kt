package com.foyer.hbc.presentation.resume

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseFragment
import com.foyer.hbc.databinding.FragmentResultBinding
import com.foyer.hbc.domain.data.resume.CheckoutState
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.presentation.resume.bills.BillsAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResumeFragment :
    BaseFragment<FragmentResultBinding>(),
    ResumeContract.ViewCapabilities,
    ResumeContract.ViewEvent {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getViewBinding(): FragmentResultBinding {
        return FragmentResultBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val resumeViewModel: ResumeViewModel by viewModel()
    private var billsAdapter: BillsAdapter? = null

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun initUI() {
        initBillsRecyclerView()
    }

    override fun initData() {
        resumeViewModel.getOrder()
    }

    override fun collectData() {
        lifecycleScope.launch {
            resumeViewModel.orderState.collect {
                when (it) {
                    CheckoutState.Loading -> showLoader()
                    is CheckoutState.Success -> {
                        binding?.checkoutComponent?.init(
                            it.checkout.actualAmount,
                            it.checkout.unPayed,
                            it.checkout.actualAmount + it.checkout.unPayed - it.checkoutBalance
                        )
                        billsAdapter?.setData(it.bills)
                        hideLoader()
                    }
                    CheckoutState.Error -> {
                        hideLoader()
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - VIEW EVENT
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickBill(billURl: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(billURl))
        startActivity(browserIntent)
    }

    override fun onClickPayedBill(bill: PaymentEntity) {
        showConfirmationPaymentBillDialog(bill)
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION - VIEW CAPABILITIES
    ///////////////////////////////////////////////////////////////////////////

    override fun showConfirmationPaymentBillDialog(bill: PaymentEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.resume_confirm_bill_payment)
            .setPositiveButton(R.string.common_validate) { _, _ ->
                resumeViewModel.payBill(bill)
            }
            .create()
            .show()
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun initBillsRecyclerView() {
        billsAdapter = BillsAdapter(this)
        binding?.checkoutBillsRecyclerview?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = billsAdapter
        }
    }
}