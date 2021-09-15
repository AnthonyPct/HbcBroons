package com.foyer.hbc.presentation.users.select

import com.foyer.hbc.domain.data.dashboard.SelectUserState
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.flow.StateFlow

interface ISelectUserContract {

    interface ViewModel {
        val selectUserState: StateFlow<SelectUserState>
        fun getUsers()
    }

    interface ViewEvent {
        fun onClickUser(userEntity: UserEntity)
    }
}
