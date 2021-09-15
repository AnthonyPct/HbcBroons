package com.foyer.hbc.presentation.match

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.data.api.ffhb.FfhbRepository
import com.foyer.hbc.domain.data.matchs.MatchState
import com.foyer.hbc.domain.data.matchs.TEAM
import com.foyer.hbc.domain.model.MatchEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MatchViewModel(application: Application, private val repository: FfhbRepository) :
    BaseViewModel(application), MatchContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////


    companion object {
        val MatchModule = module {
            viewModel { MatchViewModel(androidApplication(), get()) }
        }
        const val BROONS_TEAMS_NAME = "BROONS"
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var mutableState = MutableStateFlow<MatchState>(MatchState.Loading)
    private var oldMatchs: List<MatchEntity>? = null

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override val state: StateFlow<MatchState>
        get() = mutableState

    override fun getMatchs(team: TEAM) {
        viewModelScope.launch {
            try {
                val match = repository.getMatchs(team)
                mutableState.value = MatchState.HasMatch(match)
            } catch (e: Exception) {
                mutableState.value = MatchState.Error
            }
        }
    }

    override fun applyFilter() {
        oldMatchs = (state.value as? MatchState.HasMatch)?.matchs
        oldMatchs?.let {
            mutableState.value = MatchState.HasMatch(it.filter {
                it.firstTeam?.contains(BROONS_TEAMS_NAME, true) == true ||
                        it.secondTeam?.contains(BROONS_TEAMS_NAME, true) == true
            })
        }
    }

    override fun resetFilter() {
        oldMatchs?.let { mutableState.value = MatchState.HasMatch(it) }
    }
}