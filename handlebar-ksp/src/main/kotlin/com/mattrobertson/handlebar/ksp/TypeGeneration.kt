package com.mattrobertson.handlebar.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.TypeParameterResolver
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.File

internal fun generateTypedStateInstance(
    codeGenerator: CodeGenerator,
    spec: TypedStateSpec
) {

    val implName = "${spec.superInterface.simpleName}Impl"

    val fileSpec = FileSpec.builder(
        packageName = spec.superInterface.packageName,
        fileName = implName
    ).addType(
        TypeSpec.classBuilder(implName).apply {
            spec.props.forEach { prop ->
                addProperty(
                    PropertySpec.builder(
                        prop.name,
                        prop.type.toTypeName(TypeParameterResolver.EMPTY),
                        KModifier.OVERRIDE
                    )
                    .mutable(true)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return handle[\"${prop.name}\"]")
                            .build()
                    )
                    .setter(
                        FunSpec.setterBuilder()
                            .addParameter("value", prop.type.toTypeName(TypeParameterResolver.EMPTY))
                            .addStatement("handle[\"${prop.name}\"] = value")
                            .build()
                    )
                    .build()
                )
            }
        }
        .superclass(
            ClassName(
                packageName = "com.mattrobertson.handlebar",
                "AbstractTypedState"
            )
        )
        .addSuperclassConstructorParameter("handle")
        .addSuperinterface(spec.superInterface)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("handle", ClassName("androidx.lifecycle", "SavedStateHandle"))
                .build()
        )
        .build()
    )
    .build()

    fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
}