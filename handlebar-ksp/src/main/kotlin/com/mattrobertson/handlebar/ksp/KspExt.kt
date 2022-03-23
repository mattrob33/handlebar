package com.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

val KSType.defaultValue: Any?
    get() = when (this.toClassName()) {
        Int::class.asClassName() -> 0
        Double::class.asClassName() -> 0.0
        Float::class.asClassName() -> 0F
        String::class.asClassName() -> "\"\""
        else -> null
    }

