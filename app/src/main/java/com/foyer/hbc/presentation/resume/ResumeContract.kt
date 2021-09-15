package com.foyer.hbc.presentation.resume

import com.foyer.hbc.domain.data.resume.CheckoutState
import com.foyer.hbc.domain.model.PaymentEntity
import kotlinx.coroutines.flow.StateFlow

interface ResumeContract {

    interface ViewModel {
        val orderState: StateFlow<CheckoutState>
        fun getOrder()
        fun addBill(price: Double)
        fun payBill(bill: PaymentEntity)
    }

    interface ViewCapabilities {
        fun showConfirmationPaymentBillDialog(bill: PaymentEntity)
    }

    interface ViewEvent {
        fun onClickBill(billURl: String)
        fun onClickPayedBill(bill: PaymentEntity)
    }
}