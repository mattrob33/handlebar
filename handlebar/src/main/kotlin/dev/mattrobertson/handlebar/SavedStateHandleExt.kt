package dev.mattrobertson.handlebar

import androidx.lifecycle.SavedStateHandle

inline fun <reified T: Any> SavedStateHandle.asType(): T {
    val implType = Class.forName("${T::class.qualifiedName}Impl")
    return implType.getDeclaredConstructor(SavedStateHandle::class.java).newInstance(this) as T
}