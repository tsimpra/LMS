package gr.apt.lms.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration

internal const val SUFFIX = "_"
internal const val PACKAGE_TARGET = "gr.apt.lms.metamodel"
internal const val ENTITY_CLASS_TARGET = "entity"
internal const val DTO_CLASS_TARGET = "dto"

internal fun metaModelCreator(environment: SymbolProcessorEnvironment, resolver: Resolver) {
    //val (packageTarget, classTarget, _) = Triple("gr.apt.lms","persistence.entity","")
    //retrieveClassName(environment.options, "metamodel.target")
    //val suffix = environment.options["metamodel.suffix"] ?: "MetaModel"

    val classes = mutableSetOf<ClassType>()
    val dtos = mutableSetOf<ClassType>()
    val registry = LinkedHashMap<ClassType, Collection<FieldProperty>>()
    resolver.getSymbolsWithAnnotation(ENTITY)
        .filterIsInstance<KSClassDeclaration>()
        .forEach {
            classes.add(getAllClassDeclarations(it, registry)?.first ?: return@forEach)
        }
    resolver.getSymbolsWithAnnotation(DTO)
        .filterIsInstance<KSClassDeclaration>()
        .forEach {
            dtos.add(getAllClassDeclarations(it, registry)?.first ?: return@forEach)
        }
    if (registry.isEmpty()) return


    classes.generateMetamodel(environment, registry, ENTITY_CLASS_TARGET)
    dtos.generateMetamodel(environment, registry, DTO_CLASS_TARGET)
}

private fun Collection<ClassType>.generateMetamodel(
    environment: SymbolProcessorEnvironment,
    registry: LinkedHashMap<ClassType, Collection<FieldProperty>>,
    classTarget: String
) {
    this.forEach {
        val fields = registry[it] ?: return@forEach
        val className = it.typeName
        val metaModelClassName = className + SUFFIX
        val packageName = "$PACKAGE_TARGET.$classTarget"

        val out =
            environment.codeGenerator.createNewFile(Dependencies(false), packageName, metaModelClassName)
                .bufferedWriter()

        out.append("package $packageName\n\n")

        out.append("object $metaModelClassName {\n")
        fields.forEach { field ->
            out.append("    const val ${field.name.screamingSnakeCase()} = \"${field.name}\"\n")
        }
        out.append("}\n")
        out.flush()
        out.close()
    }
}

private fun String.screamingSnakeCase(): String {
    if (isBlank()) return this
    val screamingSnakeCase = StringBuilder((length * 1.5).toInt())

    this.toCharArray().forEach { }
    for (c in this) {
        when (c) {
            in 'a'..'z' -> screamingSnakeCase.append(c.uppercaseChar())
            in 'A'..'Z' -> screamingSnakeCase.append("_$c")
            in '0'..'9', '_' -> screamingSnakeCase.append(c)
            // we might need to add more accepted characters, but it's highly discouraged
            else -> throw IllegalArgumentException("Invalid character in camel case identifier: $c ( original string: $this)")
        }
    }
    return screamingSnakeCase.toString()
}