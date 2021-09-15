package com.foyer.hbc.presentation.users.add

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.domain.data.users.AddUserState
import com.foyer.hbc.domain.usecase.users.AddUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class AddUserViewModel(application: Application) :
    BaseViewModel(application),
    AddUserContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val AddUserModule = module {
            viewModel { AddUserViewModel(androidApplication()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val addUserUseCase: AddUserUseCase by inject(AddUserUseCase::class.java)

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    // The UI collects from this StateFlow to get its state updates
    override val addUserState: StateFlow<AddUserState>
        get() = mutableFLow

    // Backing property to avoid state updates from other classes
    private val mutableFLow = MutableStateFlow<AddUserState>(AddUserState.InitialState)

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override fun addUser(name: String, team: String) {
        viewModelScope.launch {
            try {
                addUserUseCase.execute(name, team)
                mutableFLow.value = AddUserState.Success
            } catch (e: Exception) {
                mutableFLow.value = AddUserState.Error
            }
        }
    }
}