package com.mattrobertson.handlebar.sample.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mattrobertson.handlebar.annotation.TypedState
import com.mattrobertson.handlebar.typedAs

class MainViewModel(
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val state = stateHandle typedAs MainState::class

    val name = state.name
}

@TypedState
interface MainState {
    var name: String?
    var age: Int?
}