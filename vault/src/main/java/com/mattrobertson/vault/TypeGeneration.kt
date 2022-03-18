package com.mattrobertson.vault

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.TypeParameterResolver
import com.squareup.kotlinpoet.ksp.writeTo

internal fun generateTypedStateInstance(
    codeGenerator: CodeGenerator,
    spec: TypedStateSpec
) {
    val implName = "${spec.interfaceName}Impl"

    val fileSpec = FileSpec.builder(
        packageName = spec.packageName,
        fileName = implName
    ).apply {
        TypeSpec.classBuilder(implName).apply {
            spec.props.forEach { prop ->
                addProperty(
                    PropertySpec.builder(
                        prop.name,
                        prop.type,
                        KModifier.OVERRIDE
                    ).build()
                )
            }
        }
    }.build()

    fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
}