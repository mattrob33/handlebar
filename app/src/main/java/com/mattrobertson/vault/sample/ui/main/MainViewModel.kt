package com.mattrobertson.vault.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mattrobertson.vault.typedAs

class MainViewModel(
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val state = stateHandle typedAs MainState::class

    init {

    }

}

interface MainState {
    val name: String
    val age: Int
    val count: LiveData<Int>
}