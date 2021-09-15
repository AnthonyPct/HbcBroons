package com.foyer.hbc.domain.data.resume

import com.foyer.hbc.domain.model.CheckoutEntity
import com.foyer.hbc.domain.model.PaymentEntity

sealed class CheckoutState {
    object Loading : CheckoutState()
    data class Success(
        val checkout: CheckoutEntity,
        val bills: List<PaymentEntity>,
        val checkoutBalance: Double
    ) : CheckoutState()

    object Error : CheckoutState()
}