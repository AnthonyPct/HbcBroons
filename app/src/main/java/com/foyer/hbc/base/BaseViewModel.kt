package com.foyer.hbc.base

import android.app.Application
import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////

    init {
        onCreated()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // LIFE CYCLE
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @CallSuper
    fun onCreated() {
        Log.d(this.javaClass.simpleName, "onCreated")
    }

    @CallSuper
    override fun onCleared() {
        Log.d(this.javaClass.simpleName, "onCleared")
        super.onCleared()
    }
}
