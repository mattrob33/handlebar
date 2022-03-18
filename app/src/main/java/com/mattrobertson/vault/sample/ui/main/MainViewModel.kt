package com.mattrobertson.vault.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mattrobertson.vault.BaseState
import com.mattrobertson.vault.Vault
import com.mattrobertson.vault.typedAs
import kotlin.reflect.KClass

class MainViewModel(
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val state = stateHandle typedAs MainState::class

    init {
        state.
    }

}

abstract class MainStateZ(stateHandle: SavedStateHandle): BaseState(stateHandle) {
    abstract val name: String
    abstract val age: Int
    abstract val count: LiveData<Int>
}






///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////




// This is the best one yet... would need to generate code (ksp?) from @TypedState


// USER CODE

@TypedState
interface PersonState {
    var name: String
}


val state = savedStateHandle typedAs PersonState::class


// LIBRARY CODE

infix fun <T: BaseState> SavedStateHandle.typedAs(type: KClass<T>): T {
    return type.java.getDeclaredConstructor(SavedStateHandle::class.java).newInstance(this)
}

abstract class AbstractTypedState(protected val handle: SavedStateHandle)



// GENERATED CODE

class PersonStateImpl(handle: SavedStateHandle): AbstractTypedState(handle), PersonState {
    override var name: String
        get() = handle["state_name"] ?: ""
        set(value) { handle["state_name"] = value }
}



///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////






private val state = stateHandle typedAs MainState::class

interface MainState {
    val name: String
    val age: Int
    val count: LiveData<Int>
}


/*


state.name = "Matt"
    state.handle["state_name"] = "Matt"


return state.name
    return state.handle["state_name"]


 */