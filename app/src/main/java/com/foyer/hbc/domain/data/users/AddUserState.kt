package com.foyer.hbc.domain.data.users

sealed class AddUserState {
    object InitialState : AddUserState()
    object Success : AddUserState()
    object Error : AddUserState()
}
