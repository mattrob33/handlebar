package com.mattrobertson.vault

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.TypeParameterResolver
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.reflect.KClass

@KotlinPoetKspPreview
class TypedStateSymbolProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

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

            if (!classDeclaration.javaClass.isInterface) {
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

            generateTypedStateInstance(
                codeGenerator = codeGenerator,
                spec = TypedStateSpec(
                    interfaceName = qualifiedName,
                    packageName = classDeclaration.packageName.asString(),
                    props = properties
                )
            )
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            if (!property.isMutable) {
                logger.error(
                    "@TypedState can only contain mutable properties.",
                    property
                )
                return
            }

            properties.add(
                Property(
                    name = property.simpleName.asString(),
                    type = property.type.resolve().toTypeName(TypeParameterResolver.EMPTY)
                )
            )
        }

    }
}

internal data class TypedStateSpec(
    val interfaceName: String,
    val packageName: String,
    val props: List<Property>
)

internal data class Property(
    val name: String,
    val type: TypeName
)