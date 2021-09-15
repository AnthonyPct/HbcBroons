package com.foyer.hbc.domain.data.users

import com.foyer.hbc.domain.model.PaymentEntity

sealed class PaymentState {
    object InitialState : PaymentState()
    object Loading : PaymentState()
    data class Success(val amount: Double, val userName: String) : PaymentState()
    data class HasPayments(val payments: List<PaymentEntity>) : PaymentState()
    object NoPaymentAvailable : PaymentState()
    object Error : PaymentState()
}