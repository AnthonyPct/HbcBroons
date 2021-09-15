package com.foyer.hbc.presentation.users

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.data.users.PaymentState
import com.foyer.hbc.domain.data.users.UsersState
import com.foyer.hbc.domain.model.PaymentType
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.domain.usecase.users.AddPaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class UsersViewModel(
    application: Application,
    val databaseRepository: DatabaseRepository
) :
    BaseViewModel(application),
    IUsersContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val UsersViewModelModule = module {
            viewModel { UsersViewModel(androidApplication(), get()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val addPaymentUseCase: AddPaymentUseCase by inject(
        AddPaymentUseCase::class.java
    )

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    // The UI collects from this StateFlow to get its state updates
    override val users: StateFlow<UsersState>
        get() = mutableUserFlow

    // Backing property to avoid state updates from other classes
    private val mutableUserFlow = MutableStateFlow<UsersState>(UsersState.Loading)

    override val paymentState: StateFlow<PaymentState>
        get() = mutablePaymentState

    private val mutablePaymentState =
        MutableStateFlow<PaymentState>(PaymentState.InitialState)

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override var creditAmount: Double? = 0.0
    override var paymentMode: PaymentType = PaymentType.CB
    override var isCreditPopupOpen: Boolean = false

    override fun getUsers() {
        viewModelScope.launch {
            try {
                databaseRepository.getUser().collect {
                    mutableUserFlow.value = UsersState.Success(it.sortedBy { user -> user.nom })
                }
            } catch (e: Exception) {
                mutableUserFlow.value = UsersState.Error
            }
        }
    }

    override fun getPayments(user: UserEntity) {
        viewModelScope.launch {
            val payments = databaseRepository.getPayment(user.nom)
            mutablePaymentState.value = if (payments.isNotEmpty())
                PaymentState.HasPayments(payments)
            else PaymentState.NoPaymentAvailable
        }
    }

    override fun addUserPayment(amount: Double, user: UserEntity, paymentMode: PaymentType) {
        viewModelScope.launch {
            try {
                mutablePaymentState.value = PaymentState.Loading
                addPaymentUseCase.execute(amount, user, paymentMode)
                mutablePaymentState.value = PaymentState.Success(amount, user.nom)
            } catch (e: Exception) {
                mutablePaymentState.value = PaymentState.Error
            }
        }
    }
}