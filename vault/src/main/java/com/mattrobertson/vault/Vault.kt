package com.mattrobertson.vault

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class Vault {

    companion object {

        /**
         *
         */
        fun <T: Any> wrap(state: SavedStateHandle, asType: KClass<T>): T {
            require(asType.java.isInterface) { "Vault.create() only accepts an interface" }

            asType.declaredMemberProperties.forEach { prop ->

            }
        }

    }
}

infix fun <T: Any> SavedStateHandle.typedAs(intf: KClass<T>): T {
    return Vault.wrap(this, intf)
}