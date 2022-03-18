package com.mattrobertson.vault

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass


infix fun <T: AbstractTypedState> SavedStateHandle.typedAs(type: KClass<T>): T {
    return type.java.getDeclaredConstructor(SavedStateHandle::class.java).newInstance(this)
}

abstract class AbstractTypedState(protected val handle: SavedStateHandle)