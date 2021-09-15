package com.foyer.hbc.presentation.resume

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.data.api.firebase.FirebaseRepository
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.data.resume.CheckoutState
import com.foyer.hbc.domain.model.CheckoutEntity
import com.foyer.hbc.domain.model.PaymentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ResumeViewModel(
    application: Application,
    val databaseRepository: DatabaseRepository,
    val firebaseRepository: FirebaseRepository
) :
    BaseViewModel(application),
    ResumeContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val ResumeModule = module {
            viewModel { ResumeViewModel(androidApplication(), get(), get()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    override val orderState: StateFlow<CheckoutState>
        get() = _orderState

    private val _orderState = MutableStateFlow<CheckoutState>(CheckoutState.Loading)

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getOrder() {
        viewModelScope.launch {
            try {
                combine(
                    databaseRepository.getCheckout(),
                    databaseRepository.getBills()
                ) { checkout: CheckoutEntity, bills: List<PaymentEntity> ->
                    Pair(checkout, bills)
                }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _orderState.value =
                            CheckoutState.Success(
                                it.first,
                                it.second,
                                getCurrentBillAmount(it.second)
                            )
                    }
            } catch (e: Exception) {
                _orderState.value = CheckoutState.Error
            }
        }
    }

    override fun payBill(bill: PaymentEntity) {
        viewModelScope.launch {
            joinAll(
                async { databaseRepository.payBill(bill) },
                async { databaseRepository.updateCheckoutAfterBillPayment(bill.price) },
                async { firebaseRepository.payBill(bill) },
                async { firebaseRepository.updateCheckoutAfterBillPayment(bill.price) }
            )
        }
    }

    override fun addBill(price: Double) {

    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun getCurrentBillAmount(bills: List<PaymentEntity>): Double {
        return bills
            .filter { it.isPayed.not() && it.charge }
            .sumOf { it.price }
    }
}