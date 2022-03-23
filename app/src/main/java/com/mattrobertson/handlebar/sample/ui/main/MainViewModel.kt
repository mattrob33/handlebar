package com.mattrobertson.handlebar.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mattrobertson.handlebar.annotation.TypedState
import com.mattrobertson.handlebar.asType

class MainViewModel(
    stateHandle: SavedStateHandle
) : ViewModel() {

    val test = stateHandle.getLiveData<String>("")
    val test2 = stateHandle.getLiveData<String>("", "")

    private val state = stateHandle.asType<MainStateContract>()

    val name = state.name

}

@TypedState
interface MainState {
    var name: String
    val age: LiveData<Int>
}