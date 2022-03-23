package com.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.mattrobertson.handlebar.annotation.TypedState
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName

@KotlinPoetKspPreview
class TypedStateSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        resolver.builtIns.intType

        val symbols = resolver.getSymbolsWithAnnotation(TypedState::class.qualifiedName!!)
        val unableToProcess = symbols.filterNot { it.validate() }

        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(Visitor(), Unit) }

        return unableToProcess.toList()
    }

    private inner class Visitor: KSVisitorVoid() {

        val properties = mutableListOf<Property>()

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

            classDeclaration.getAllProperties()
                .forEach {
                    it.accept(this, Unit)
                }

            generateInterfaceAndImplementation(
                codeGenerator = codeGenerator,
                spec = TypedStateSpec(
                    superInterface = classDeclaration.toClassName(),
                    props = properties
                )
            )
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            val type = property.type.resolve()
            val isLiveData = (type.toClassName().simpleName == "LiveData")

            when {
                property.isMutable && isLiveData -> {
                    logger.error(
                        "LiveData variables marked with @TypedState must be immutable.",
                        property
                    )
                    return
                }
                !property.isMutable && !isLiveData -> {
                    logger.error(
                        "Variables marked with @TypedState must be mutable.",
                        property
                    )
                    return
                }
            }

            properties.add(
                Property(
                    name = property.simpleName.asString(),
                    type = property.type.resolve()
                )
            )
        }

    }
}

internal data class TypedStateSpec(
    val superInterface: ClassName,
    val props: List<Property>
)

internal data class Property(
    val name: String,
    val type: KSType
)