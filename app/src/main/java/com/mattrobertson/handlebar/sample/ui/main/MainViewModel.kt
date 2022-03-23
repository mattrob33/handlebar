package com.mattrobertson.handlebar.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mattrobertson.handlebar.annotation.TypedState
import com.mattrobertson.handlebar.asType

class MainViewModel(
    stateHandle: SavedStateHandle
) : ViewModel() {

    @TypedState interface State {
        var name: String
        val age: LiveData<Int>
        val pet: LiveData<Pet>
    }

    private val state = stateHandle.asType<StateContract>()

    val age = state.age
    val pet = state.pet

    var name: String
        get() = state.name
        set(value) { state.name = value }

    fun increaseAge() {
        state.updateAge(age.value!! + 1)
    }
}

data class Pet(
    val name: String,
    val species: Species
)

enum class Species {
    Dog,
    Cat,
    Bird,
    Fish
}