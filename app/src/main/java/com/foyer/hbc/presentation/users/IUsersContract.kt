package com.foyer.hbc.presentation.users

import com.foyer.hbc.domain.data.users.PaymentState
import com.foyer.hbc.domain.data.users.UsersState
import com.foyer.hbc.domain.model.PaymentType
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.flow.StateFlow

interface IUsersContract {

    interface ViewModel {
        var creditAmount: Double?
        var paymentMode: PaymentType
        var isCreditPopupOpen: Boolean
        val users: StateFlow<UsersState>
        val paymentState: StateFlow<PaymentState>

        fun getUsers()
        fun getPayments(user: UserEntity)
        fun addUserPayment(amount: Double, user: UserEntity, paymentMode: PaymentType)
    }

    interface ViewCapabilities {
        fun initCreditView(user: UserEntity)
        fun openCreditView(user: UserEntity)
        fun closeCreditView()
        fun showPasswordDialog()
        fun showSuccessCreditPopup()
        fun showFailedPasswordPopup()
        fun showGeneralErrorPopup()
    }

    interface ViewNavigation {
        fun redirectToAddUSerPopup()
    }
}