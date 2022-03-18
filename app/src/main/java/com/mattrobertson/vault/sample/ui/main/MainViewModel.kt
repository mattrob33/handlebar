package com.mattrobertson.vault.sample.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mattrobertson.vault.TypedState
import com.mattrobertson.vault.typedAs

class MainViewModel(
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val state = stateHandle typedAs MainState::class

}

@TypedState
interface MainState {
    var name: String
    var age: Int
}