package com.foyer.hbc.domain.data.users

import com.foyer.hbc.domain.model.UserEntity

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<UserEntity>) : UsersState()
    object Error : UsersState()
}