package com.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.ClassName

internal class TypedStateGenerator(
    private val codeGenerator: CodeGenerator
) {
    fun generate(
        sourceInterface: ClassName,
        properties: List<TypedStateProperty>
    ){
        TypedStateContractGenerator(codeGenerator)
            .generate(sourceInterface, properties)

        TypedStateImplGenerator(codeGenerator)
            .generate(sourceInterface, properties)
    }
}