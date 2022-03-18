package com.mattrobertson.vault

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class TypedStateSymbolProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        TODO("Not yet implemented")
    }

}