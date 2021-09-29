package com.foyer.hbc.presentation.match

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.data.api.ffhb.FfhbRepository
import com.foyer.hbc.domain.data.matchs.MatchState
import com.foyer.hbc.domain.data.matchs.TEAM
import com.foyer.hbc.domain.model.MatchEntity
import com.foyer.hbc.domain.model.isBroonsMatch
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
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var mutableState = MutableStateFlow<MatchState>(MatchState.Loading)
    private var oldMatchs: List<MatchEntity>? = null
    private var filterActive: Boolean = false

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override val state: StateFlow<MatchState>
        get() = mutableState

    override fun getMatchs(team: TEAM) {
        mutableState.value = MatchState.Loading
        viewModelScope.launch {
            try {
                oldMatchs = repository.getMatchs(team)
                oldMatchs?.let {
                    mutableState.value = MatchState.HasMatch(
                        if (filterActive) {
                            it.filter { match -> match.isBroonsMatch() }
                        } else it
                    )
                }
            } catch (e: Exception) {
                mutableState.value = MatchState.Error
            }
        }
    }

    override fun applyFilter() {
        filterActive = true
        oldMatchs?.let {
            mutableState.value = MatchState.HasMatch(
                it.filter { match -> match.isBroonsMatch() }
            )
        }
    }

    override fun resetFilter() {
        filterActive = false
        oldMatchs?.let { mutableState.value = MatchState.HasMatch(it) }
    }
}