package com.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

internal fun generateInterfaceAndImplementation(
    codeGenerator: CodeGenerator,
    spec: TypedStateSpec
) {
    generateInterface(codeGenerator, spec)
    generateImplementation(codeGenerator, spec)
}

private fun generateInterface(
    codeGenerator: CodeGenerator,
    spec: TypedStateSpec
) {
    val interfaceName = "${spec.superInterface.simpleName}Contract"

    val fileSpec = FileSpec.builder(
        packageName = spec.superInterface.packageName,
        fileName = interfaceName
    ).addType(
        TypeSpec.interfaceBuilder(interfaceName).apply {
            spec.props.forEach { prop ->
                prop.buildInterfaceSpec(this)
            }
        }
        .build()
    )
    .build()

    fileSpec.writeTo(codeGenerator, aggregating = false)
}

private fun generateImplementation(
    codeGenerator: CodeGenerator,
    spec: TypedStateSpec
) {
    val superName = "${spec.superInterface.simpleName}Contract"
    val implName = "${spec.superInterface.simpleName}ContractImpl"

    val fileSpec = FileSpec.builder(
        packageName = spec.superInterface.packageName,
        fileName = implName
    ).addType(
        TypeSpec.classBuilder(implName).apply {
            spec.props.forEach { prop ->
                prop.buildSpec(this)
            }
        }
            .superclass(
                ClassName(
                    packageName = "com.mattrobertson.handlebar",
                    "AbstractTypedState"
                )
            )
            .addSuperclassConstructorParameter("handle")
            .addSuperinterface(ClassName(spec.superInterface.packageName, superName))
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("handle", ClassName("androidx.lifecycle", "SavedStateHandle"))
                    .build()
            )
            .build()
    )
    .build()

    fileSpec.writeTo(codeGenerator, aggregating = false)
}