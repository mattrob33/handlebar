package dev.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * An abstract class for generating a file containing a single type declaration.
 */
internal abstract class SingleTypeFileGenerator(
    private val codeGenerator: CodeGenerator
) {
    fun generateFile(
        packageName: String,
        className: String,
        typeSpec: TypeSpec
    ) {
        val fileSpec = FileSpec.builder(
            packageName = packageName,
            fileName = className
        )
        .addType(typeSpec)
        .build()

        fileSpec.writeTo(codeGenerator, aggregating = false)
    }
}