package com.foyer.hbc.presentation.users.add

import com.foyer.hbc.domain.data.users.AddUserState
import kotlinx.coroutines.flow.StateFlow

interface AddUserContract {

    interface ViewModel {
        val addUserState: StateFlow<AddUserState>
        fun addUser(name: String, team: String)
    }

    interface ViewCapabilities {
        fun showFailedPopup()
    }
}