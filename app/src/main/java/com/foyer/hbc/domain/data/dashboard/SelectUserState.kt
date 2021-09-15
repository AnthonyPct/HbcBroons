package com.foyer.hbc.domain.data.dashboard

import com.foyer.hbc.domain.model.UserEntity

sealed class SelectUserState {
    object Loading : SelectUserState()
    data class HasUsers(val users: List<UserEntity>) : SelectUserState()
    object Error : SelectUserState()
}
