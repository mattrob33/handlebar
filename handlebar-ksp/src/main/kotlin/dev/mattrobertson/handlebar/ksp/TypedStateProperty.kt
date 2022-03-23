package dev.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType

internal data class TypedStateProperty(
    val name: String,
    val type: KSType
)

internal fun KSPropertyDeclaration.asTypedStateProperty(): TypedStateProperty {
    return TypedStateProperty(
        name = simpleName.asString(),
        type = type.resolve()
    )
}