package com.mattrobertson.handlebar

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass

infix fun <T: Any> SavedStateHandle.typedAs(type: KClass<T>): T {
    val implType = Class.forName("${type.simpleName}Impl")
    return implType.getDeclaredConstructor(SavedStateHandle::class.java).newInstance(this) as T
}