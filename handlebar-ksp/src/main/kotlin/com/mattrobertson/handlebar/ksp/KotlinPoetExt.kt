package com.mattrobertson.handlebar.ksp

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.TypeParameterResolver
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal fun Property.buildSpec(typeBuilder: TypeSpec.Builder) {
    val isNullable = type.toTypeName().isNullable
    val isLiveData = (type.toClassName().simpleName == "LiveData")

    typeBuilder.addProperty(
        if (isNullable) {
            buildNullablePropSpec()
        }
        else {
            if (isLiveData)
                buildNonNullableLiveDataPropSpec()
            else
                buildNonNullablePropSpec()
        }
    )

    if (isLiveData) {
        typeBuilder.addFunction(
            buildLiveDataPropSpec()
        )
    }
}

internal fun Property.buildInterfaceSpec(typeBuilder: TypeSpec.Builder) {
    val isLiveData = (type.toClassName().simpleName == "LiveData")

    typeBuilder.addProperty(
        PropertySpec.builder(name, type.toTypeName())
            .mutable(!isLiveData)
            .build()
    )

    if (isLiveData) {
        val parameterizedType = type.arguments[0].type

        typeBuilder.addFunction(
            FunSpec.builder("update${name.capitalize()}")
                .addParameter("value", parameterizedType!!.toTypeName())
                .build()
        )
    }
}

private fun Property.buildNullablePropSpec(): PropertySpec {
    return PropertySpec.builder(
        name,
        type.toTypeName(TypeParameterResolver.EMPTY),
        KModifier.OVERRIDE
    )
        .mutable(true)
        .getter(
            FunSpec.getterBuilder()
                .addStatement("return handle[\"${name}\"]")
                .build()
        )
        .setter(
            FunSpec.setterBuilder()
                .addParameter("value", type.toTypeName(TypeParameterResolver.EMPTY))
                .addStatement("handle[\"${name}\"] = value")
                .build()
        )
        .build()
}

private fun Property.buildNonNullablePropSpec(): PropertySpec {
    return PropertySpec.builder(
        name,
        type.toTypeName(TypeParameterResolver.EMPTY),
        KModifier.OVERRIDE
    )
        .mutable(true)
        .getter(
            FunSpec.getterBuilder()
                .addStatement("return handle[\"${name}\"] ?: ${type.defaultValue}")
                .build()
        )
        .setter(
            FunSpec.setterBuilder()
                .addParameter("value", type.toTypeName(TypeParameterResolver.EMPTY))
                .addStatement("handle[\"${name}\"] = value")
                .build()
        )
        .build()
}

private fun Property.buildNonNullableLiveDataPropSpec(): PropertySpec {
    val parameterizedType = type.arguments[0].type

    return PropertySpec.builder(
        name,
        type.toTypeName(TypeParameterResolver.EMPTY),
        KModifier.OVERRIDE
    )
        .mutable(false)
        .getter(
            FunSpec.getterBuilder()
                .addStatement("return handle.getLiveData<${parameterizedType}>(\"${name}\", ${parameterizedType!!.resolve().defaultValue})")
                .build()
        )
        .build()
}

private fun Property.buildLiveDataPropSpec(): FunSpec {
    val parameterizedType = type.arguments[0].type

    return FunSpec.builder("update${name.capitalize()}")
        .addModifiers(KModifier.OVERRIDE)
        .addParameter("value", parameterizedType!!.toTypeName())
        .addStatement("handle.getLiveData<${parameterizedType}>(\"${name}\").value = `value`")
        .build()
}