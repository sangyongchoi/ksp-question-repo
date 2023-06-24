package com.example.docsgenerator

import com.example.docsgenerator.annotation.DemoAnnotation
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

class DemoProcessor : SymbolProcessor {

    private val targetAnnotation = DemoAnnotation::class.java

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val elements =
            resolver.getSymbolsWithAnnotation("${targetAnnotation.packageName}.${targetAnnotation.simpleName}")

        if (elements.none()) {
            return emptyList()
        }

        val ret = elements.filter { !it.validate() }.toList()

        elements
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                val classDeclaration = (it as KSClassDeclaration)

                classDeclaration.qualifiedName?.asString()?.also { qualifiedName ->
                    val demo = Class.forName(qualifiedName)
                    println(demo)
                }
            }

        return ret
    }
}
