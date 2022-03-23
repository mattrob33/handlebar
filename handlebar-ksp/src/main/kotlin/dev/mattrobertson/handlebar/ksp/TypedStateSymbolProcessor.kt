package dev.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import dev.mattrobertson.handlebar.annotation.TypedState
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName

@KotlinPoetKspPreview
class TypedStateSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbols = resolver.getSymbolsWithAnnotation(TypedState::class.qualifiedName!!)

        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(Visitor(), Unit) }

        val unprocessedSymbols = symbols.filterNot { it.validate() }.toList()
        return unprocessedSymbols
    }

    private inner class Visitor: KSVisitorVoid() {

        val properties = mutableListOf<TypedStateProperty>()

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val qualifiedName = classDeclaration.qualifiedName?.asString() ?: run {
                logger.error(
                    "@TypedState can only be applied to classes with qualified names",
                    classDeclaration
                )
                return
            }

            if (classDeclaration.classKind != ClassKind.INTERFACE) {
                logger.error(
                    "@TypedState can only be applied to interfaces. $qualifiedName is not an interface.",
                    classDeclaration
                )
                return
            }

            classDeclaration.getAllProperties().forEach { property ->
                property.accept(this, Unit)
            }

            TypedStateGenerator(codeGenerator)
                .generate(
                    sourceInterface = classDeclaration.toClassName(),
                    properties = properties
                )
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            properties.add(property.asTypedStateProperty())
        }
    }
}