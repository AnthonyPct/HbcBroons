package com.foyer.hbc.presentation.users.select

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.data.dashboard.SelectUserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class SelectUserViewModel(
    application: Application,
    val databaseRepository: DatabaseRepository
) :
    BaseViewModel(application),
    ISelectUserContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGUATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val SelectUserModule = module {
            viewModel { SelectUserViewModel(androidApplication(), get()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    // The UI collects from this StateFlow to get its state updates
    override val selectUserState: StateFlow<SelectUserState>
        get() = mutableFLow

    // Backing property to avoid state updates from other classes
    private val mutableFLow = MutableStateFlow<SelectUserState>(SelectUserState.Loading)

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getUsers() {
        viewModelScope.launch {
            try {
                databaseRepository.getUser().collect {
                    mutableFLow.value = SelectUserState.HasUsers(it.sortedBy { user -> user.nom })
                }
            } catch (e: Exception) {
                mutableFLow.value = SelectUserState.Error
            }
        }
    }
}
